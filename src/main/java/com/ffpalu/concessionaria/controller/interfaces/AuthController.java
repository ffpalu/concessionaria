package com.ffpalu.concessionaria.controller.interfaces;

import com.ffpalu.concessionaria.dto.request.LoginRequest;
import com.ffpalu.concessionaria.dto.response.AuthResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public interface AuthController {

	public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest loginRequest);
}
