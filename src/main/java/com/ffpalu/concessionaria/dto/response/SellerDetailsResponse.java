package com.ffpalu.concessionaria.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
public class SellerDetailsResponse {

    private UUID id;

    private String employeeCode;

    private LocalDate hireDate;

    private String businessPhone;

    private UserResponse userDetails;

}
