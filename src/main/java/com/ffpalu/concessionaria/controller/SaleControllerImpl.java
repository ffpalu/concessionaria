package com.ffpalu.concessionaria.controller;

import com.ffpalu.concessionaria.controller.interfaces.SaleController;
import com.ffpalu.concessionaria.dto.request.SaleWrappedRequest;
import com.ffpalu.concessionaria.dto.response.SaleResponse;
import com.ffpalu.concessionaria.middleware.interfaces.SaleMiddleware;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class SaleControllerImpl implements SaleController {

    private final SaleMiddleware saleMiddleware;

    @Override
    public ResponseEntity<SaleResponse> createSale(SaleWrappedRequest request, Authentication auth) {
       SaleResponse saleResponse = saleMiddleware.createSale(request, auth.getName());
       return ResponseEntity.status(HttpStatus.CREATED).body(saleResponse);
    }

    @Override
    public ResponseEntity<Page<SaleResponse>> getSaleOfSeller(String sellerId, Pageable pageable) {
        Page<SaleResponse> salesOfSeller = saleMiddleware.getSaleOfSeller(UUID.fromString(sellerId), pageable);
        return ResponseEntity.ok(salesOfSeller);
    }

    @Override
    public ResponseEntity<Page<SaleResponse>> getSaleOrderedByMostValue(Pageable pageable) {
        Page<SaleResponse> salesOrderedByValue = saleMiddleware.getTopSale(pageable);
        return ResponseEntity.ok(salesOrderedByValue);
    }
}
