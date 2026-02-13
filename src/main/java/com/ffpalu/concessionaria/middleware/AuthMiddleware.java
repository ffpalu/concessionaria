package com.ffpalu.concessionaria.middleware;

import com.ffpalu.concessionaria.dto.request.LoginRequest;
import com.ffpalu.concessionaria.dto.response.AuthResponse;
import com.ffpalu.concessionaria.entity.Credential;
import com.ffpalu.concessionaria.entity.User;
import com.ffpalu.concessionaria.exceptions.BadCredential;
import com.ffpalu.concessionaria.security.JwtService;
import com.ffpalu.concessionaria.service.CredentialServiceImpl;
import com.ffpalu.concessionaria.service.UserServiceImpl;
import com.ffpalu.concessionaria.utils.Mapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthMiddleware {

	private final CredentialServiceImpl credentialService;
	private final UserServiceImpl userService;
	private final AuthenticationManager authenticationManager;
	private final JwtService jwtService;

	public AuthResponse login(LoginRequest credentialLogin) {

		Authentication authentication =  authenticationManager.authenticate(
						new UsernamePasswordAuthenticationToken(credentialLogin.getUsername(), credentialLogin.getPassword())
		);

		Credential credential = credentialService.getCredentialByUsername(credentialLogin.getUsername())
						.orElseThrow(() -> new BadCredential("Credential not found")); // non dovrebbe accadere in quanto in fase di autenticazione fa già il controllo

		String token = jwtService.generateToken(credential);

		User user = userService.getUserByUsername(credentialLogin.getUsername())
						.orElseThrow(() -> new BadCredential("User not found"));


		return Mapper.mapToLoginResponse(token, user);


	}




}
