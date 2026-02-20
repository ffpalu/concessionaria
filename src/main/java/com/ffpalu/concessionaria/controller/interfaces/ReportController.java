package com.ffpalu.concessionaria.controller.interfaces;


import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@SecurityRequirement(name = "bearerAuth")
@RequestMapping("/api/report")
public interface ReportController {

	@GetMapping("/monthly-revenue")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<String> triggerJobMonthlyRevenue();

}
