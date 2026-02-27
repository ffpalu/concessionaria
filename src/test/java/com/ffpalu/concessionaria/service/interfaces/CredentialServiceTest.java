package com.ffpalu.concessionaria.service.interfaces;

import com.ffpalu.concessionaria.dto.request.CredentialRequest;
import com.ffpalu.concessionaria.entity.Credential;
import com.ffpalu.concessionaria.entity.User;
import com.ffpalu.concessionaria.entity.enums.Role;
import com.ffpalu.concessionaria.exceptions.BadCredential;
import com.ffpalu.concessionaria.repository.CredentialRepository;
import com.ffpalu.concessionaria.service.CredentialServiceImpl;
import com.ffpalu.concessionaria.utils.Mapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CredentialServiceTest {

    @Mock
    private CredentialRepository credentialRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private Mapper mapper;

    @InjectMocks
    private CredentialServiceImpl credentialService;

    private User user;
    private Credential credential;
    private CredentialRequest credentialRequest;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(UUID.randomUUID())
                .firstName("Mario")
                .lastName("Rossi")
                .CF("RSSMRA90A01H501Z")
                .email("mario@test.com")
                .build();

        credential = Credential.builder()
                .id(UUID.randomUUID())
                .username("mario.rossi")
                .password("encodedPassword")
                .role(Role.ADMIN)
                .user(user)
                .build();

        credentialRequest = new CredentialRequest();
        credentialRequest.setUsername("mario.rossi");
        credentialRequest.setPassword("Password123");
        credentialRequest.setRole(Role.ADMIN);
    }


    @Test
    void getCredentialByUsernameShouldReturnCredentialWhenExists() {
        when(credentialRepository.findByUsername("mario.rossi")).thenReturn(Optional.of(credential));

        Optional<Credential> result = credentialService.getCredentialByUsername("mario.rossi");

        assertTrue(result.isPresent());
        assertEquals("mario.rossi", result.get().getUsername());
        verify(credentialRepository).findByUsername("mario.rossi");
    }

    @Test
    void getCredentialByUsernameShouldReturnEmptyWhenNotExists() {
        when(credentialRepository.findByUsername("nonexistent")).thenReturn(Optional.empty());

        Optional<Credential> result = credentialService.getCredentialByUsername("nonexistent");

        assertTrue(result.isEmpty());
        verify(credentialRepository).findByUsername("nonexistent");
    }

    // === createCredential ===

    @Test
    void createCredentialShouldSaveAndReturnWhenUsernameNotExists() {
        when(credentialRepository.existsByUsername("mario.rossi")).thenReturn(false);
        when(mapper.mapToCredential(credentialRequest, user)).thenReturn(credential);
        when(credentialRepository.save(credential)).thenReturn(credential);

        Credential result = credentialService.createCredential(credentialRequest, user);

        assertNotNull(result);
        assertEquals("mario.rossi", result.getUsername());
        assertEquals(Role.ADMIN, result.getRole());
        verify(credentialRepository).existsByUsername("mario.rossi");
        verify(mapper).mapToCredential(credentialRequest, user);
        verify(credentialRepository).save(credential);
    }

    @Test
    void createCredentialShouldThrowWhenUsernameAlreadyExists() {
        when(credentialRepository.existsByUsername("mario.rossi")).thenReturn(true);

        assertThrows(BadCredential.class, () -> credentialService.createCredential(credentialRequest, user));
        verify(credentialRepository, never()).save(any());
        verify(mapper, never()).mapToCredential(any(), any());
    }

    // === changePassword ===

    @Test
    void changePasswordShouldEncodeAndSaveWhenUserExists() {
        Credential updatedCredential = credential.withPassword("newEncodedPassword");

        when(credentialRepository.findByUsername("mario.rossi")).thenReturn(Optional.of(credential));
        when(passwordEncoder.encode("newPassword")).thenReturn("newEncodedPassword");
        when(credentialRepository.save(any(Credential.class))).thenReturn(updatedCredential);

        credentialService.changePassword("mario.rossi", "newPassword");

        verify(credentialRepository).findByUsername("mario.rossi");
        verify(passwordEncoder).encode("newPassword");
        verify(credentialRepository).save(any(Credential.class));
    }

    @Test
    void changePasswordShouldThrowWhenUserNotFound() {
        when(credentialRepository.findByUsername("nonexistent")).thenReturn(Optional.empty());

        assertThrows(BadCredential.class, () -> credentialService.changePassword("nonexistent", "newPassword"));
        verify(passwordEncoder, never()).encode(any());
        verify(credentialRepository, never()).save(any());
    }

    // === checkIfCredentialExists ===

    @Test
    void checkIfCredentialExistsShouldReturnTrueWhenExists() {
        when(credentialRepository.existsByUsername("mario.rossi")).thenReturn(true);

        assertTrue(credentialService.checkIfCredentialExists("mario.rossi"));
    }

    @Test
    void checkIfCredentialExistsShouldReturnFalseWhenNotExists() {
        when(credentialRepository.existsByUsername("nonexistent")).thenReturn(false);

        assertFalse(credentialService.checkIfCredentialExists("nonexistent"));
    }
}