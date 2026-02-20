package com.ffpalu.concessionaria.dto.support;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
public class SaleUnwrappedDTO {

    private LocalDate sellDate;

    private Double price;

    private UUID customer;

    private UUID vehicle;

    private UUID seller;

}
