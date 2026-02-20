package com.ffpalu.concessionaria.dto.support;

import com.ffpalu.concessionaria.entity.Customer;
import com.ffpalu.concessionaria.entity.Seller;
import com.ffpalu.concessionaria.entity.Vehicle;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
public class SaleDTO {

    private LocalDate sellDate;

    private Double price;

    private UUID customer;

    private UUID vehicle;

    private UUID seller;

}
