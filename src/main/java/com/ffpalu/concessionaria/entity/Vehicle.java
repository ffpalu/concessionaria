package com.ffpalu.concessionaria.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;


@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Vehicle {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;

	@Column(nullable = false, unique = true)
	private String plate;

	private String brand;
	private String model;
	private Boolean used;

	@Min(value = 1950, message = "Vehicle too old")
	private Integer year;

	@Min(value = 0, message = "il numero minimo di km deve essere 0")
	private Integer numberOfKilometers;

	@OneToOne(mappedBy = "vehicle")
	private Sale sale;

	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;


	@PrePersist
	protected void onCreate() {
		this.createdAt = LocalDateTime.now();
		this.updatedAt = LocalDateTime.now();
	}

	@PreUpdate
	protected void onUpdate() {
		this.updatedAt = LocalDateTime.now();
	}


}
