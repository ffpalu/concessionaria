package com.ffpalu.concessionaria.middleware.interfaces;

import com.ffpalu.concessionaria.dto.request.SaleRequest;
import com.ffpalu.concessionaria.dto.response.SaleResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface SaleMiddleware {

    SaleResponse createSale(SaleRequest request, String username);

    Page<SaleResponse> getSaleOfSeller(UUID id, Pageable pageable);

    Page<SaleResponse> getTopSale(Pageable pageable);

}
