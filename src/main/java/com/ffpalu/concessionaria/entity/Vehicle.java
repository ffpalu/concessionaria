package com.ffpalu.concessionaria.entity;

import com.ffpalu.concessionaria.annotation.interfaces.MinYear;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.Year;
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

	@PastOrPresent(message = "Year min 1950")
	@MinYear(value = 1950, message = "vehicle too old")
	private Year year;

	@PositiveOrZero(message = "number of kilometer must be at least 0")
	private Integer numberOfKilometers;

	@OneToOne(mappedBy = "vehicle")
	private Sale sale;

	@Column(name = "created_at")
	private LocalDateTime createdAt;

	@Column(name = "updated_at")
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
