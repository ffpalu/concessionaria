package com.ffpalu.concessionaria.dto.request;

import com.ffpalu.concessionaria.dto.validation.CredentialValidationGroup;
import com.ffpalu.concessionaria.entity.enums.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CredentialRequest {


	@NotBlank(message = "usernameRequired")
	@Size(min = 3, message = "username must be t least 3 characters long")
	private String username;

	@NotBlank(message = "password required")
	@Size(min = 3, message = "password must be at least 3 characters long")
	private String password;

	@NotNull(groups = CredentialValidationGroup.Create.class, message = "il ruolo è necessaria per la registrazione utente")
	private Role role;

}
