package com.ffpalu.concessionaria.controller.interfaces;

import com.ffpalu.concessionaria.dto.request.SaleRequest;
import com.ffpalu.concessionaria.dto.response.SaleResponse;
import com.ffpalu.concessionaria.entity.Sale;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@SecurityRequirement(name = "bearerAuth")
@RequestMapping("/api/sale")
public interface SaleController {

    @PreAuthorize("hasRole('ROLE_SELLER')")
    @PutMapping("/create")
    ResponseEntity<SaleResponse> createSale(
            @RequestBody
            SaleRequest request,
            Authentication auth
    );

    @GetMapping("/{sellerId}")
    ResponseEntity<Page<SaleResponse>> getSaleOfSeller(
            @PathVariable String sellerId,
            Pageable pageable
    );

    @GetMapping("/topSale")
    ResponseEntity<Page<SaleResponse>> getSaleOrderedByMostValue(Pageable pageable);


}
