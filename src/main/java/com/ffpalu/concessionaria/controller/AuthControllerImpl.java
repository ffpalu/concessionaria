package com.ffpalu.concessionaria.controller;

import com.ffpalu.concessionaria.controller.interfaces.AuthController;
import com.ffpalu.concessionaria.dto.request.LoginRequest;
import com.ffpalu.concessionaria.dto.response.AuthResponse;
import com.ffpalu.concessionaria.middleware.AuthMiddleware;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthControllerImpl implements AuthController {

	private final AuthMiddleware authMiddleware;

	@PostMapping("/login")
	public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest loginRequest) {
		return ResponseEntity.ok(authMiddleware.login(loginRequest));
	}



}
