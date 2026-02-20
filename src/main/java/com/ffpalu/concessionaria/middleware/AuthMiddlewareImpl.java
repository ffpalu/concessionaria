package com.ffpalu.concessionaria.middleware;

import com.ffpalu.concessionaria.dto.request.CredentialRequest;
import com.ffpalu.concessionaria.dto.request.UserDetailsRequest;
import com.ffpalu.concessionaria.dto.request.SellerDetailsRequest;
import com.ffpalu.concessionaria.dto.request.RegistrationWrapperRequest;
import com.ffpalu.concessionaria.dto.response.AuthResponse;
import com.ffpalu.concessionaria.entity.Credential;
import com.ffpalu.concessionaria.entity.Seller;
import com.ffpalu.concessionaria.entity.User;
import com.ffpalu.concessionaria.entity.enums.Role;
import com.ffpalu.concessionaria.exceptions.BadCredential;
import com.ffpalu.concessionaria.exceptions.UserException;
import com.ffpalu.concessionaria.middleware.interfaces.AuthMiddleware;
import com.ffpalu.concessionaria.security.JwtService;
import com.ffpalu.concessionaria.service.CredentialServiceImpl;
import com.ffpalu.concessionaria.service.UserServiceImpl;
import com.ffpalu.concessionaria.service.interfaces.SellerService;
import com.ffpalu.concessionaria.utils.Mapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthMiddlewareImpl  implements AuthMiddleware {

	private final CredentialServiceImpl credentialService;
	private final UserServiceImpl userService;
	private final SellerService sellerService;
	private final AuthenticationManager authenticationManager;
	private final JwtService jwtService;
	private final Mapper mapper;

	@Override
	public AuthResponse login(CredentialRequest credentialLogin) {

		Authentication authentication =  authenticationManager.authenticate(
						new UsernamePasswordAuthenticationToken(credentialLogin.getUsername(), credentialLogin.getPassword())
		);

		Credential credential = credentialService.getCredentialByUsername(credentialLogin.getUsername())
						.orElseThrow(() -> new BadCredential("Credential not found")); // non dovrebbe accadere in quanto in fase di autenticazione fa già il controllo

		String token = jwtService.generateToken(credential);

		User user = userService.getUserByUsername(credentialLogin.getUsername())
						.orElseThrow(() -> new BadCredential("User not found"));


		return mapper.mapToLoginResponse(token, user);


	}

	@Transactional
	@Override
	public void registerUser(RegistrationWrapperRequest request) {
		UserDetailsRequest userDetails = request.getUser();
		CredentialRequest credentialRequest = request.getCredential();

		SellerDetailsRequest sellerDetails = request.getDetails();

		if (userService.checkIfUserExists(userDetails.getCF(), userDetails.getEmail())) {
			throw new UserException("User already exists");
		}

		if (credentialService.checkIfCredentialExists(credentialRequest.getUsername())) {
			throw new BadCredential("Username already exists");
		}

		User userCreated = userService.createUser(userDetails);

		Credential credential = credentialService.createCredential(credentialRequest, userCreated);

		if (credentialRequest.getRole() == Role.SELLER) {
			Seller seller = sellerService.createSeller(sellerDetails, userCreated);
		}

	}

	@Override
	@Transactional
	public void changePassword(Authentication authentication, String newPassword) {
		credentialService.changePassword(authentication.getName(), newPassword);
	}
}
