package com.ffpalu.concessionaria.middleware.interfaces;

import com.ffpalu.concessionaria.dto.request.CredentialRequest;
import com.ffpalu.concessionaria.dto.request.RegistrationWrapperRequest;
import com.ffpalu.concessionaria.dto.response.AuthResponse;
import org.springframework.security.core.Authentication;

public interface AuthMiddleware {

    AuthResponse login(CredentialRequest credentialLogin);

    void registerUser(RegistrationWrapperRequest request);


    void changePassword(Authentication authentication, String newPassword);

}
