package com.ffpalu.concessionaria.middleware.interfaces;

import com.ffpalu.concessionaria.dto.request.SaleWrappedRequest;
import com.ffpalu.concessionaria.dto.response.CustomerResponse;
import com.ffpalu.concessionaria.dto.response.SaleResponse;
import com.ffpalu.concessionaria.dto.response.SellerDetailsResponse;
import com.ffpalu.concessionaria.dto.response.VehicleResponse;
import com.ffpalu.concessionaria.dto.support.SaleUnwrappedDTO;
import com.ffpalu.concessionaria.entity.*;
import com.ffpalu.concessionaria.exceptions.CustomerException;
import com.ffpalu.concessionaria.exceptions.SellerException;
import com.ffpalu.concessionaria.exceptions.VehicleException;
import com.ffpalu.concessionaria.middleware.SaleMiddlewareImpl;
import com.ffpalu.concessionaria.service.interfaces.CustomerService;
import com.ffpalu.concessionaria.service.interfaces.SaleService;
import com.ffpalu.concessionaria.service.interfaces.SellerService;
import com.ffpalu.concessionaria.service.interfaces.VehicleService;
import com.ffpalu.concessionaria.utils.Mapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.Year;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;



@ExtendWith(MockitoExtension.class)
class SaleMiddlewareTest {
    @Mock private SaleService saleService;
    @Mock private SellerService sellerService;
    @Mock private CustomerService customerService;
    @Mock private VehicleService vehicleService;
    @Mock private Mapper mapper;

    @InjectMocks
    private SaleMiddlewareImpl saleMiddleware;

    private Vehicle vehicle;
    private Customer customer;
    private Seller seller;
    private Sale sale;
    private SaleWrappedRequest request;

    @BeforeEach
    void setUp() {
        vehicle = Vehicle.builder()
                .id(UUID.randomUUID()).plate("AB123CD").brand("Fiat").model("Panda")
                .used(false).year(Year.of(2023)).sale(null) // non venduto
                .build();

        customer = Customer.builder()
                .id(UUID.randomUUID()).firstName("Luigi").lastName("Verdi")
                .CF("VRDLGU85B02F205X").email("luigi@test.com")
                .build();

        User sellerUser = User.builder()
                .id(UUID.randomUUID()).firstName("Claudio").lastName("Piccardi")
                .CF("PCDCLD75A12D969K").email("claudio@test.com")
                .build();

        seller = Seller.builder()
                .id(UUID.randomUUID()).user(sellerUser)
                .employeeCode("EMP001").build();

        sale = Sale.builder()
                .id(UUID.randomUUID()).sellDate(LocalDate.now()).price(15000.0)
                .sellerId(seller.getId()).customerId(customer.getId()).vehicleId(vehicle.getId())
                .build();

        request = new SaleWrappedRequest();
        request.setSellDate(LocalDate.now());
        request.setPrice(15000.0);
        request.setCustomerCF("VRDLGU85B02F205X");
        request.setVehiclePlate("AB123CD");
    }

    // === createSale ===

    @Test
    void createSaleShouldReturnResponseWhenAllEntitiesExist() {
        SaleResponse expectedResponse = SaleResponse.builder().id(sale.getId()).price(15000.0).build();

        when(vehicleService.getVehicleByPlate("AB123CD")).thenReturn(Optional.of(vehicle));
        when(customerService.getCustomerByCF("VRDLGU85B02F205X")).thenReturn(Optional.of(customer));
        when(sellerService.findByUsername("claudio.piccardi")).thenReturn(Optional.of(seller));
        when(saleService.createSale(any(SaleUnwrappedDTO.class))).thenReturn(sale);
        when(mapper.mapToSellerDetailsResponse(seller)).thenReturn(SellerDetailsResponse.builder().build());
        when(mapper.mapToCustomerResponse(customer)).thenReturn(CustomerResponse.builder().build());
        when(mapper.mapToVehicleResponse(vehicle)).thenReturn(VehicleResponse.builder().build());
        when(mapper.mapToSaleResponse(eq(sale), any(), any(), any())).thenReturn(expectedResponse);

        SaleResponse result = saleMiddleware.createSale(request, "claudio.piccardi");

        assertNotNull(result);
        assertEquals(15000.0, result.getPrice());
        verify(saleService).createSale(any(SaleUnwrappedDTO.class));
    }

    @Test
    void createSaleShouldThrowWhenVehicleNotFound() {
        when(vehicleService.getVehicleByPlate("AB123CD")).thenReturn(Optional.empty());

        assertThrows(VehicleException.class, () -> saleMiddleware.createSale(request, "claudio.piccardi"));
        verify(saleService, never()).createSale(any());
    }

    @Test
    void createSaleShouldThrowWhenVehicleAlreadySold() {
        Vehicle soldVehicle = Vehicle.builder()
                .id(UUID.randomUUID()).plate("AB123CD")
                .sale(Sale.builder().id(UUID.randomUUID()).build()) // già venduto
                .build();

        when(vehicleService.getVehicleByPlate("AB123CD")).thenReturn(Optional.of(soldVehicle));

        assertThrows(VehicleException.class, () -> saleMiddleware.createSale(request, "claudio.piccardi"));
        verify(saleService, never()).createSale(any());
    }

    @Test
    void createSaleShouldThrowWhenCustomerNotFound() {
        when(vehicleService.getVehicleByPlate("AB123CD")).thenReturn(Optional.of(vehicle));
        when(customerService.getCustomerByCF("VRDLGU85B02F205X")).thenReturn(Optional.empty());

        assertThrows(CustomerException.class, () -> saleMiddleware.createSale(request, "claudio.piccardi"));
        verify(saleService, never()).createSale(any());
    }

    @Test
    void createSaleShouldThrowWhenSellerNotFound() {
        when(vehicleService.getVehicleByPlate("AB123CD")).thenReturn(Optional.of(vehicle));
        when(customerService.getCustomerByCF("VRDLGU85B02F205X")).thenReturn(Optional.of(customer));
        when(sellerService.findByUsername("unknown")).thenReturn(Optional.empty());

        assertThrows(SellerException.class, () -> saleMiddleware.createSale(request, "unknown"));
        verify(saleService, never()).createSale(any());
    }

    // === getSaleOfSeller ===

    @Test
    void getSaleOfSellerShouldDelegateToServiceAndMap() {
        Pageable pageable = PageRequest.of(0, 10);
        sale.setSeller(seller); sale.setCustomer(customer); sale.setVehicle(vehicle);
        Page<Sale> page = new PageImpl<>(List.of(sale));

        when(saleService.getSaleOfSeller(seller.getId(), pageable)).thenReturn(page);
        when(mapper.mapToSaleResponse(any(Sale.class))).thenReturn(SaleResponse.builder().build());

        Page<SaleResponse> result = saleMiddleware.getSaleOfSeller(seller.getId(), pageable);

        assertEquals(1, result.getTotalElements());
    }

    // === getTopSale ===

    @Test
    void getTopSaleShouldApplyPriceDescendingSort() {
        Pageable pageable = PageRequest.of(0, 10);
        sale.setSeller(seller); sale.setCustomer(customer); sale.setVehicle(vehicle);
        Page<Sale> page = new PageImpl<>(List.of(sale));

        when(saleService.getAllSale(any(Pageable.class))).thenReturn(page);
        when(mapper.mapToSaleResponse(any(Sale.class))).thenReturn(SaleResponse.builder().build());

        Page<SaleResponse> result = saleMiddleware.getTopSale(pageable);

        assertEquals(1, result.getTotalElements());
        verify(saleService).getAllSale(argThat(p -> p.getSort().getOrderFor("price") != null));
    }
}