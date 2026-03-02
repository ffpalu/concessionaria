package com.ffpalu.concessionaria.middleware.interfaces;

import com.ffpalu.concessionaria.dto.request.CredentialRequest;
import com.ffpalu.concessionaria.dto.request.RegistrationWrapperRequest;
import com.ffpalu.concessionaria.dto.request.SellerDetailsRequest;
import com.ffpalu.concessionaria.dto.request.UserDetailsRequest;
import com.ffpalu.concessionaria.dto.response.AuthResponse;
import com.ffpalu.concessionaria.entity.Credential;
import com.ffpalu.concessionaria.entity.User;
import com.ffpalu.concessionaria.entity.enums.Role;
import com.ffpalu.concessionaria.exceptions.BadCredential;
import com.ffpalu.concessionaria.exceptions.UserException;
import com.ffpalu.concessionaria.middleware.AuthMiddlewareImpl;
import com.ffpalu.concessionaria.security.JwtService;
import com.ffpalu.concessionaria.service.CredentialServiceImpl;
import com.ffpalu.concessionaria.service.UserServiceImpl;
import com.ffpalu.concessionaria.service.interfaces.SellerService;
import com.ffpalu.concessionaria.utils.Mapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthMiddlewareTest {

    @Mock
    private CredentialServiceImpl credentialService;
    @Mock
    private UserServiceImpl userService;
    @Mock
    private SellerService sellerService;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private JwtService jwtService;
    @Mock
    private Mapper mapper;

    @InjectMocks
    private AuthMiddlewareImpl authMiddleware;

    private User user;
    private Credential credential;
    private CredentialRequest credentialRequest;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(UUID.randomUUID())
                .firstName("Giorgio").lastName("Strada")
                .CF("SCPGRC02A25M452X").email("giorgio@test.com")
                .build();

        credential = Credential.builder()
                .id(UUID.randomUUID())
                .username("giorgio.strada")
                .password("encoded")
                .role(Role.ADMIN)
                .user(user)
                .build();

        credentialRequest = new CredentialRequest();
        credentialRequest.setUsername("giorgio.strada");
        credentialRequest.setPassword("Password123");
        credentialRequest.setRole(Role.ADMIN);
    }


    @Test
    void loginShouldReturnAuthResponseWhenCredentialsAreValid() {
        Authentication auth = mock(Authentication.class);
        AuthResponse expectedResponse = AuthResponse.builder().token("jwt-token").build();

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(auth);
        when(credentialService.getCredentialByUsername("giorgio.strada")).thenReturn(Optional.of(credential));
        when(jwtService.generateToken(credential)).thenReturn("jwt-token");
        when(userService.getUserByUsername("giorgio.strada")).thenReturn(Optional.of(user));
        when(mapper.mapToLoginResponse("jwt-token", user)).thenReturn(expectedResponse);

        AuthResponse result = authMiddleware.login(credentialRequest);

        assertNotNull(result);
        assertEquals("jwt-token", result.getToken());
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
    }

    @Test
    void loginShouldThrowWhenCredentialNotFound() {
        Authentication auth = mock(Authentication.class);
        when(authenticationManager.authenticate(any())).thenReturn(auth);
        when(credentialService.getCredentialByUsername("giorgio.strada")).thenReturn(Optional.empty());

        assertThrows(BadCredential.class, () -> authMiddleware.login(credentialRequest));
    }


    @Test
    void registerUserShouldCreateUserAndCredentialForNonSellerRole() {
        UserDetailsRequest userReq = new UserDetailsRequest();
        userReq.setFirstName("Mario"); userReq.setLastName("Rossi");
        userReq.setCF("RSSMRA90A01H501Z"); userReq.setEmail("mario@test.com");

        CredentialRequest credReq = new CredentialRequest();
        credReq.setUsername("mario.rossi"); credReq.setPassword("Pass123"); credReq.setRole(Role.SUPPORT);

        RegistrationWrapperRequest request = new RegistrationWrapperRequest();
        request.setUser(userReq);
        request.setCredential(credReq);
        request.setDetails(null);

        when(userService.checkIfUserExists("RSSMRA90A01H501Z", "mario@test.com")).thenReturn(false);
        when(credentialService.checkIfCredentialExists("mario.rossi")).thenReturn(false);
        when(userService.createUser(userReq)).thenReturn(user);
        when(credentialService.createCredential(credReq, user)).thenReturn(credential);

        authMiddleware.registerUser(request);

        verify(userService).createUser(userReq);
        verify(credentialService).createCredential(credReq, user);
        verify(sellerService, never()).createSeller(any(), any());
    }

    @Test
    void registerUserShouldAlsoCreateSellerWhenRoleIsSeller() {
        UserDetailsRequest userReq = new UserDetailsRequest();
        userReq.setFirstName("Claudio"); userReq.setLastName("Piccardi");
        userReq.setCF("PCDCLD75A12D969K"); userReq.setEmail("claudio@test.com");

        CredentialRequest credReq = new CredentialRequest();
        credReq.setUsername("claudio.piccardi"); credReq.setPassword("Pass123"); credReq.setRole(Role.SELLER);

        SellerDetailsRequest sellerReq = new SellerDetailsRequest();
        sellerReq.setEmployeeCode("EMP001");

        RegistrationWrapperRequest request = new RegistrationWrapperRequest();
        request.setUser(userReq);
        request.setCredential(credReq);
        request.setDetails(sellerReq);

        Credential sellerCred = credential.withRole(Role.SELLER);

        when(userService.checkIfUserExists(any(), any())).thenReturn(false);
        when(credentialService.checkIfCredentialExists(any())).thenReturn(false);
        when(userService.createUser(userReq)).thenReturn(user);
        when(credentialService.createCredential(credReq, user)).thenReturn(sellerCred);

        authMiddleware.registerUser(request);

        verify(sellerService).createSeller(sellerReq, user);
    }

    @Test
    void registerUserShouldThrowWhenUserAlreadyExists() {
        UserDetailsRequest userReq = new UserDetailsRequest();
        userReq.setCF("SCPGRC02A25M452X"); userReq.setEmail("giorgio@test.com");

        RegistrationWrapperRequest request = new RegistrationWrapperRequest();
        request.setUser(userReq);
        request.setCredential(credentialRequest);

        when(userService.checkIfUserExists("SCPGRC02A25M452X", "giorgio@test.com")).thenReturn(true);

        assertThrows(UserException.class, () -> authMiddleware.registerUser(request));
        verify(userService, never()).createUser(any());
    }

    @Test
    void registerUserShouldThrowWhenUsernameAlreadyExists() {
        UserDetailsRequest userReq = new UserDetailsRequest();
        userReq.setCF("NEWCF12345678901"); userReq.setEmail("new@test.com");

        RegistrationWrapperRequest request = new RegistrationWrapperRequest();
        request.setUser(userReq);
        request.setCredential(credentialRequest);

        when(userService.checkIfUserExists(any(), any())).thenReturn(false);
        when(credentialService.checkIfCredentialExists("giorgio.strada")).thenReturn(true);

        assertThrows(BadCredential.class, () -> authMiddleware.registerUser(request));
        verify(userService, never()).createUser(any());
    }


    @Test
    void changePasswordShouldDelegateToCredentialService() {
        Authentication auth = mock(Authentication.class);
        when(auth.getName()).thenReturn("giorgio.strada");

        authMiddleware.changePassword(auth, "newPassword");

        verify(credentialService).changePassword("giorgio.strada", "newPassword");
    }

}