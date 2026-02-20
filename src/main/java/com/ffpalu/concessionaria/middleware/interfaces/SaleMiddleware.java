package com.ffpalu.concessionaria.middleware.interfaces;

import com.ffpalu.concessionaria.dto.request.SaleWrappedRequest;
import com.ffpalu.concessionaria.dto.response.SaleResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface SaleMiddleware {

    SaleResponse createSale(SaleWrappedRequest request, String username);

    Page<SaleResponse> getSaleOfSeller(UUID id, Pageable pageable);

    Page<SaleResponse> getTopSale(Pageable pageable);

}
