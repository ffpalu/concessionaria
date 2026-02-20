package com.ffpalu.concessionaria.service.interfaces;


import com.ffpalu.concessionaria.dto.support.SaleDTO;
import com.ffpalu.concessionaria.entity.Sale;
import com.ffpalu.concessionaria.entity.Seller;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface SaleService {

    Sale createSale(SaleDTO saleToCreate);

    Sale updateSale(SaleDTO saleToUpdate, UUID id);

    Page<Sale> getSaleOfSeller(UUID seller, Pageable pageable);

    Page<Sale> getAllSale(Pageable pageable);


}
