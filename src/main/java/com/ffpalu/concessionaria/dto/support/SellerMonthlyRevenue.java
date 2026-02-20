package com.ffpalu.concessionaria.dto.support;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SellerMonthlyRevenue {
	private UUID sellerId;
	private String employeeCode;
	private String firstName;
	private String lastName;
	Double totalRevenue;

}
