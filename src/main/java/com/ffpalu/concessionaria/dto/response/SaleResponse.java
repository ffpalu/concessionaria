package com.ffpalu.concessionaria.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
public class SaleResponse {

    private UUID id;

    private LocalDate sellDate;

    private Double price;

    private SellerDetailsResponse seller;

    private CustomerResponse customer;

    private VehicleResponse vehicle;
}
