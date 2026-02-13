package com.ffpalu.concessionaria.dto.request;

import com.ffpalu.concessionaria.entity.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegistrationRequest {
	@NotBlank
	@Size(min = 3 , message = "first name must be at least 3 characters long")
	private String firstName;

	@NotBlank
	@Size(min = 3 , message = "last name must be at least 3 characters long")
	private String lastName;

	@Email(message = "Insert a valid mail")
	private String email;

	@NotBlank
	@Size(min = 3, message = "username must be t least 3 characters long")
	private String username;

	@NotBlank
	@Size(min = 3, message = "password must be at least 3 characters long")
	private String password;

	@NotNull
	private Role role;
}
