package com.ffpalu.concessionaria.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Formula;

import java.util.List;
import java.util.UUID;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Customer {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;


	@Column(nullable = false, name = "first_name")
	private String firstName;

	@Column(nullable = false, name = "last_name")
	private String lastName;

	@Column
	@Email(message = "Insert a valid email")
	private String email;

	@Column(name = "fiscal_code", nullable = false, unique = true)
	private String CF;

	@Formula("(SELECT COUNT(*) FROM sale s WHERE s.customer_id = id)")
	private int salesCount;

	@OneToMany(mappedBy = "customer")
	private List<Sale> sales;


}
