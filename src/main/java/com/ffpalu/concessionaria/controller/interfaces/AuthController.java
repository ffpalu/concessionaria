package com.ffpalu.concessionaria.controller.interfaces;

import com.ffpalu.concessionaria.dto.request.CredentialRequest;
import com.ffpalu.concessionaria.dto.request.RegistrationWrapperRequest;
import com.ffpalu.concessionaria.dto.response.AuthResponse;
import com.ffpalu.concessionaria.dto.validation.CredentialValidationGroup;
import com.ffpalu.concessionaria.dto.validation.RegistrationValidationGroup;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/auth")
public interface AuthController {

	@PostMapping("/login")
    ResponseEntity<AuthResponse> login(@RequestBody @Validated(CredentialValidationGroup.Login.class) CredentialRequest loginRequest);


	@PostMapping("/register")
	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SUPPORT')")
    ResponseEntity<String> register(
            @RequestBody @Validated({CredentialValidationGroup.Create.class, RegistrationValidationGroup.RegistrationOther.class})
            RegistrationWrapperRequest request
    );

	@PostMapping("/register/seller")
	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SUPPORT')")
    ResponseEntity<String> registerSeller(
            @RequestBody @Validated({CredentialValidationGroup.Create.class, RegistrationValidationGroup.RegistrationSeller.class})
            RegistrationWrapperRequest request
    );
}
