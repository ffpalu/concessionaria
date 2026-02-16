package com.ffpalu.concessionaria.controller;

import com.ffpalu.concessionaria.controller.interfaces.AuthController;
import com.ffpalu.concessionaria.dto.request.CredentialRequest;
import com.ffpalu.concessionaria.dto.request.RegistrationWrapperRequest;
import com.ffpalu.concessionaria.dto.response.AuthResponse;
import com.ffpalu.concessionaria.dto.validation.CredentialValidationGroup;
import com.ffpalu.concessionaria.middleware.AuthMiddleware;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
public class AuthControllerImpl implements AuthController {

	private final AuthMiddleware authMiddleware;

	@Override
	public ResponseEntity<AuthResponse> login(CredentialRequest loginRequest) {
		AuthResponse response = authMiddleware.login(loginRequest);
		return ResponseEntity.ok(response);
	}


	@Override
	public ResponseEntity<String> register(RegistrationWrapperRequest request) {
		authMiddleware.registerUser(request);
		return ResponseEntity.status(HttpStatus.CREATED).body("Created User");
	}

	@Override
	public ResponseEntity<String> registerSeller(RegistrationWrapperRequest request) {
		authMiddleware.registerUser(request);
		return ResponseEntity.status(HttpStatus.CREATED).body("Created Seller");
	}
}
