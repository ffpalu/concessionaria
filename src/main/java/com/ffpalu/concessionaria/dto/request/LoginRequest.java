package com.ffpalu.concessionaria.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class LoginRequest {

	@NotBlank(message = "Username required")
	private String username;


	@NotBlank(message = "password required")
	@Size(min = 6, message = "password must be min 6 character")
	private String password;

}
