package com.ffpalu.concessionaria.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Seller {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false, unique = true)
	private User user;

	@Column(name = "employee_code", unique = true, length = 20)
	private String employeeCode;

	@Column(name = "hire_date")
	private LocalDate hireDate;

	@Column(name = "business_phone")
	private String businessPhone;


	@OneToMany(mappedBy = "seller")
	private List<Sale> sales;

}
