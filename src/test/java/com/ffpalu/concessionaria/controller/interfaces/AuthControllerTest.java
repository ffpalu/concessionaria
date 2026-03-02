package com.ffpalu.concessionaria.controller.interfaces;

import com.ffpalu.concessionaria.controller.AuthControllerImpl;
import com.ffpalu.concessionaria.dto.request.CredentialRequest;
import com.ffpalu.concessionaria.dto.request.RegistrationWrapperRequest;
import com.ffpalu.concessionaria.dto.request.UserDetailsRequest;
import com.ffpalu.concessionaria.dto.response.AuthResponse;
import com.ffpalu.concessionaria.dto.response.UserDetailsResponse;
import com.ffpalu.concessionaria.entity.enums.Role;
import com.ffpalu.concessionaria.middleware.interfaces.AuthMiddleware;
import com.ffpalu.concessionaria.security.JwtService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthControllerImpl.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @MockitoBean
    private AuthMiddleware authMiddleware;

    @MockitoBean
    private JwtService jwtService;

    @Test
    void loginShouldReturn200WithToken() throws Exception {
        CredentialRequest credentialRequest = new CredentialRequest();

        credentialRequest.setUsername("admin");
        credentialRequest.setPassword("Password123");

        AuthResponse response = AuthResponse.builder()
                .token("jwt-token")
                .type("Bearer")
                .user(
                        UserDetailsResponse.builder()
                                .firstName("giorgio")
                                .build()
                )
                .build();

        when(authMiddleware.login(any(CredentialRequest.class))).thenReturn(response);

        mockMvc.perform(
                post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(credentialRequest))
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("jwt-token"));
    }


    @Test
    @WithMockUser(roles = "ADMIN")
    void registerShouldReturn201() throws Exception {
        RegistrationWrapperRequest req = new RegistrationWrapperRequest();
        UserDetailsRequest userReq =  new UserDetailsRequest();
        userReq.setFirstName("mario");
        userReq.setLastName("Rossi");
        userReq.setCF("RSSMRA90A01H501Z");
        userReq.setEmail("mario@test.com");
        CredentialRequest credReq = new CredentialRequest();
        credReq.setUsername("mario.rossi"); credReq.setPassword("Password123"); credReq.setRole(Role.SUPPORT);
        req.setUser(userReq);
        req.setCredential(credReq);

        doNothing().when(authMiddleware).registerUser(any());

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(req)))
                .andExpect(status().isCreated());

    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void changePassword() throws Exception {
        doNothing().when(authMiddleware).changePassword(any(), any());

        mockMvc.perform(
                patch("/api/auth/changepassword")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("\"newPassword123\"")
        )
                .andExpect(status().isOk());
    }
}