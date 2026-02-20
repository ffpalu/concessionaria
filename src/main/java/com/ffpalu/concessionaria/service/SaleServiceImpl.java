package com.ffpalu.concessionaria.service;

import com.ffpalu.concessionaria.dto.support.SaleDTO;
import com.ffpalu.concessionaria.entity.Sale;
import com.ffpalu.concessionaria.entity.Seller;
import com.ffpalu.concessionaria.exceptions.SaleException;
import com.ffpalu.concessionaria.repository.SaleRepository;
import com.ffpalu.concessionaria.service.interfaces.SaleService;
import com.ffpalu.concessionaria.utils.Mapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SaleServiceImpl implements SaleService {

    private final SaleRepository saleRepository;
    private final Mapper mapper;

    @Override
    public Sale createSale(SaleDTO sale) {
        if (saleRepository.existsByVehicleId(sale.getVehicle())) {
            throw new SaleException("Vehicle already sold");
        }

        Sale saleToStore = mapper.mapToSale(sale);


        return saleRepository.save(saleToStore);
    }

    @Override
    public Sale updateSale(SaleDTO saleToUpdate, UUID id) {
        Sale sale = saleRepository.findById(id)
                .orElseThrow(() -> new SaleException("sale not found"));

        sale = Sale.builder().id(id)
                .sellDate(saleToUpdate.getSellDate() != null ? saleToUpdate.getSellDate(): sale.getSellDate())
                .price(saleToUpdate.getPrice() != null ? saleToUpdate.getPrice(): sale.getPrice())
                .sellerId(saleToUpdate.getSeller() != null ? saleToUpdate.getSeller(): sale.getSellerId())
                .customerId(saleToUpdate.getCustomer() != null ? saleToUpdate.getCustomer(): sale.getCustomerId())
                .vehicleId(saleToUpdate.getVehicle() != null ? saleToUpdate.getVehicle(): sale.getVehicleId())
                .build();

        return saleRepository.save(sale);
    }

    @Override
    public Page<Sale> getSaleOfSeller(UUID seller, Pageable pageable) {
        return saleRepository.findBySellerId(seller, pageable);
    }

    @Override
    public Page<Sale> getAllSale(Pageable pageable) {
        return saleRepository.findAll(pageable);
    }
}
