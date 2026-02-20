package com.ffpalu.concessionaria.service.interfaces;


import com.ffpalu.concessionaria.dto.support.SaleUnwrappedDTO;
import com.ffpalu.concessionaria.entity.Sale;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface SaleService {

    Sale createSale(SaleUnwrappedDTO saleToCreate);

    Sale updateSale(SaleUnwrappedDTO saleToUpdate, UUID id);

    Page<Sale> getSaleOfSeller(UUID seller, Pageable pageable);

    Page<Sale> getAllSale(Pageable pageable);


}
