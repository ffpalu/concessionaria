package com.ffpalu.concessionaria.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@With
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;

	@Column(name = "first_name", nullable = false, length = 100)
	private String firstName;

	@Column(name = "last_name", nullable = false, length = 100)
	private String lastName;

	@Column(name = "fiscal_code", nullable = false, length = 16, unique = true)
	private String CF;

	@Column(nullable = false, unique = true)
	private String email;

	@Column(nullable = false)
	@Builder.Default
	private boolean isActive = true;

	@Column(name = "created_at", nullable = false)
	private LocalDateTime createdAt;

	@Column(name = "updated_at")
	private LocalDateTime updatedAt;

	@OneToOne(mappedBy = "user")
	private Credential credential;

	@OneToOne(mappedBy = "user")
	private Seller sellerDetails;


	@PrePersist
	protected void onCreate() {
		this.createdAt = LocalDateTime.now();
		this.updatedAt = LocalDateTime.now();
	}

	@PreUpdate
	protected void onUpdate() {
		this.updatedAt = LocalDateTime.now();
	}

	public void deactivate() {
		isActive = false;
	}


}
