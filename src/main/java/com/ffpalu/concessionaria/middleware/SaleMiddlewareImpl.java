package com.ffpalu.concessionaria.middleware;

import com.ffpalu.concessionaria.dto.request.SaleWrappedRequest;
import com.ffpalu.concessionaria.dto.response.CustomerResponse;
import com.ffpalu.concessionaria.dto.response.SaleResponse;
import com.ffpalu.concessionaria.dto.support.SaleUnwrappedDTO;
import com.ffpalu.concessionaria.entity.*;
import com.ffpalu.concessionaria.exceptions.CustomerException;
import com.ffpalu.concessionaria.exceptions.SellerException;
import com.ffpalu.concessionaria.exceptions.VehicleException;
import com.ffpalu.concessionaria.middleware.interfaces.SaleMiddleware;
import com.ffpalu.concessionaria.service.interfaces.CustomerService;
import com.ffpalu.concessionaria.service.interfaces.SaleService;
import com.ffpalu.concessionaria.service.interfaces.SellerService;
import com.ffpalu.concessionaria.service.interfaces.VehicleService;
import com.ffpalu.concessionaria.utils.Mapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class SaleMiddlewareImpl implements SaleMiddleware {

    private final SaleService saleService;
    private final SellerService sellerService;
    private final CustomerService customerService;
    private final VehicleService vehicleService;
    private final Mapper mapper;

    @Override
    public SaleResponse createSale(SaleWrappedRequest request, String username) {

        Vehicle vehicleToSold = vehicleService.getVehicleByPlate(request.getVehiclePlate())
                .orElseThrow(() -> new VehicleException("Vehicle not found"));


        if (vehicleToSold.getSale() != null) {
            throw new VehicleException("Vehicle already sold");
        }


        Customer customer = customerService.getCustomerByCF(request.getCustomerCF())
                .orElseThrow(() -> new CustomerException("Customer not found"));

        Seller seller = sellerService.findByUsername(username)
                .orElseThrow( ()-> new SellerException("Seller not found"));

        SaleUnwrappedDTO saleDTO = SaleUnwrappedDTO.builder()
                .sellDate(request.getSellDate())
                .price(request.getPrice())
                .customer(customer.getId())
                .vehicle(vehicleToSold.getId())
                .seller(seller.getId())
                .build();


        Sale sale = saleService.createSale(saleDTO);

        return mapper.mapToSaleResponse(sale, mapper.mapToSellerDetailsResponse(seller), mapper.mapToCustomerResponse(customer), mapper.mapToVehicleResponse(vehicleToSold));

    }

    @Override
    public Page<SaleResponse> getSaleOfSeller(UUID id, Pageable pageable) {
        Page<Sale> sales = saleService.getSaleOfSeller(id, pageable);

        return sales.map(mapper::mapToSaleResponse);
    }

    @Override
    public Page<SaleResponse> getTopSale(Pageable pageable) {
        Pageable pageable1 = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by("price").descending());

        Page<Sale> topSales = saleService.getAllSale(pageable1);

        return topSales.map(mapper::mapToSaleResponse);
    }
}
