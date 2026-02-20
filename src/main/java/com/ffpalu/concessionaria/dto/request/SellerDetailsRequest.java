package com.ffpalu.concessionaria.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDate;



@Data
public class SellerDetailsRequest {

    @NotBlank
    private String employeeCode;

    private String businessPhone;

    private LocalDate hireDate;

}
