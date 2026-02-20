package com.ffpalu.concessionaria.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

import java.time.LocalDate;

@Data
public class SaleRequest {

    @PastOrPresent(message = "Insert a sell date past or present") //Dubbio se poter impostare date di vendita future
    private LocalDate sellDate;

    @PositiveOrZero(message = "Insert positive value")
    private Double price;

    @NotBlank
    private String customerCF;

    @NotBlank
    private String vehiclePlate;
}
