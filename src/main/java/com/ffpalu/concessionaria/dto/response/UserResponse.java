package com.ffpalu.concessionaria.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class UserResponse {
	private UUID id;
	private String firstName;
	private String lastName;
	private String email;
	private String CF;


	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
}
