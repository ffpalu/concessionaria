package com.ffpalu.concessionaria.service.interfaces;

import com.ffpalu.concessionaria.dto.support.SaleUnwrappedDTO;
import com.ffpalu.concessionaria.entity.Sale;
import com.ffpalu.concessionaria.entity.Seller;
import com.ffpalu.concessionaria.exceptions.SaleException;
import com.ffpalu.concessionaria.repository.SaleRepository;
import com.ffpalu.concessionaria.service.SaleServiceImpl;
import com.ffpalu.concessionaria.utils.Mapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SaleServiceTest {

    @Mock
    private SaleRepository saleRepository;

    @Mock
    private Mapper mapper;

    @InjectMocks
    private SaleServiceImpl saleService;

    @Test
    void createSaleShouldReturnNewSaleWhenVehicleNotAlreadySold() {
        UUID vehicleId = UUID.randomUUID();
        UUID saleId = UUID.randomUUID();
        SaleUnwrappedDTO saleUnwrappedDTO = SaleUnwrappedDTO.builder().vehicle(vehicleId).build();

        Sale sale = Sale.builder().vehicleId(vehicleId).build();
        Sale savedSale = Sale.builder().id(saleId).vehicleId(vehicleId).build();


        when(saleRepository.existsByVehicleId(vehicleId)).thenReturn(false);
        when(mapper.mapToSale(saleUnwrappedDTO)).thenReturn(sale);
        when(saleRepository.save(sale)).thenReturn(savedSale);

        Sale result = saleService.createSale(saleUnwrappedDTO);

        assertNotNull(result);
        assertEquals(savedSale, result);
        assertEquals(vehicleId, result.getVehicleId());
        assertEquals(saleId, result.getId());

        verify(saleRepository).save(any());
    }

    @Test
    void createShouldThrowExceptionWhenVehicleNotAlreadySold() {
        UUID vehicleId = UUID.randomUUID();
        SaleUnwrappedDTO saleUnwrappedDTO = SaleUnwrappedDTO.builder().vehicle(vehicleId).build();

        when(saleRepository.existsByVehicleId(vehicleId)).thenReturn(true);

        assertThrows(SaleException.class,() -> saleService.createSale(saleUnwrappedDTO));

        verify(saleRepository, never()).save(any());

    }

    @Test
    void updateSaleShouldReturnUpdatedSaleWhenSaleExists() {
        UUID vehicleId = UUID.randomUUID();
        UUID saleId = UUID.randomUUID();
        Sale sale = Sale.builder().id(saleId).vehicleId(vehicleId).price(3200.0).build();
        SaleUnwrappedDTO saleUnwrappedDTO = SaleUnwrappedDTO.builder().price(3250.0).build();
        Sale updatedSale = Sale.builder().id(saleId).vehicleId(vehicleId).price(3250.0).build();

        when(saleRepository.findById(saleId)).thenReturn(Optional.of(sale));
        when(saleRepository.save(updatedSale)).thenReturn(updatedSale);

        Sale result = saleService.updateSale(saleUnwrappedDTO, saleId);
        assertNotNull(result);
        assertEquals(updatedSale, result);
        assertEquals(vehicleId, result.getVehicleId());
        assertEquals(saleId, result.getId());
        assertEquals(3250.0, result.getPrice());

        verify(saleRepository).save(any());

    }

    @Test
    void updateSaleShouldThrowExceptionWhenSaleDoesNotExist() {
        UUID saleId = UUID.randomUUID();

        SaleUnwrappedDTO saleUnwrappedDTO = SaleUnwrappedDTO.builder().price(3250.0).build();

        when(saleRepository.findById(saleId)).thenReturn(Optional.empty());

        assertThrows(SaleException.class, () -> saleService.updateSale(saleUnwrappedDTO, saleId));
        verify(saleRepository, never()).save(any());
    }

    @Test
    void getSaleOfSellerShouldReturnPageOfSale() {
        UUID vehicleId = UUID.randomUUID();
        UUID vehicleId2 = UUID.randomUUID();
        UUID sellerId =  UUID.randomUUID();

        Sale sale1  = Sale.builder().sellerId(sellerId).vehicleId(vehicleId).price(3200.0).build();
        Sale sale2 =  Sale.builder().sellerId(sellerId).vehicleId(vehicleId2).build();

        Pageable pageable =  PageRequest.of(0, 10);

        Page<Sale> sales = new PageImpl<>(List.of(sale1, sale2), pageable, 10);

        when(saleRepository.findBySellerId(sellerId, pageable)).thenReturn(sales);

        Page<Sale> result = saleService.getSaleOfSeller(sellerId, pageable);

        assertNotNull(result);
        assertEquals(sales, result);
    }

    @Test
    void getAllSale() {
        UUID vehicleId = UUID.randomUUID();
        UUID vehicleId2 = UUID.randomUUID();
        UUID sellerId =  UUID.randomUUID();

        Sale sale1  = Sale.builder().sellerId(sellerId).vehicleId(vehicleId).price(3200.0).build();
        Sale sale2 =  Sale.builder().sellerId(sellerId).vehicleId(vehicleId2).build();

        Pageable pageable =  PageRequest.of(0, 10);

        Page<Sale> sales = new PageImpl<>(List.of(sale1, sale2), pageable, 10);

        when(saleRepository.findAll(pageable)).thenReturn(sales);


        Page<Sale> result = saleService.getAllSale(pageable);

        assertNotNull(result);
        assertEquals(sales, result);

    }
}