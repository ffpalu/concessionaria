package com.ffpalu.concessionaria.repository;

import com.ffpalu.concessionaria.config.ApplicationConfig;
import com.ffpalu.concessionaria.entity.Credential;
import com.ffpalu.concessionaria.entity.Seller;
import com.ffpalu.concessionaria.entity.User;
import com.ffpalu.concessionaria.entity.enums.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class CredentialRepositoryTest {

    @TestConfiguration
    static class CredentialRepositoryTestConfiguration {
        @Bean
        public PasswordEncoder passwordEncoder() {
            return new BCryptPasswordEncoder();
        }
    }

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CredentialRepository credentialRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;



    @BeforeEach
    void setUp() {
        User admin = User.builder()
                .firstName("Giorgio")
                .lastName("Strada")
                .CF("SCPGRC02A25M452X")
                .email("giorgio.strada@email.com")
                .build();


        User seller = User.builder()
                .firstName("Claudio")
                .lastName("Piccardi")
                .CF("PCDCLD75A12D969K")
                .email("claudio.piccardi@email.com")
                .build();


        User supporter = User.builder()
                .firstName("Giacomo")
                .lastName("Pastorino")
                .CF("PMLCSW96A25D451K")
                .email("giacomo.pastorino@email.com")
                .build();


        userRepository.saveAll(List.of(admin,supporter,seller));

        admin = userRepository.findUserByCF(admin.getCF()).get();
        seller = userRepository.findUserByCF(seller.getCF()).get();
        supporter = userRepository.findUserByCF(supporter.getCF()).get();


        Credential credentialAdmin = Credential.builder()
                .username("giorgio.strada")
                .password(passwordEncoder.encode("Password123"))
                .role(Role.ADMIN)
                .user(admin)
                .build();


        Credential credentialSupporter = Credential.builder()
                .username("giacomo.pastorino")
                .password(passwordEncoder.encode("Password1234"))
                .role(Role.SUPPORT)
                .user(supporter)
                .build();

        Credential credentialSeller = Credential.builder()
                .username("claudio.piccardi")
                .password(passwordEncoder.encode("Password1235"))
                .role(Role.SELLER)
                .user(seller)
                .build();

        credentialRepository.saveAll(List.of(credentialAdmin,credentialSupporter, credentialSeller));
    }

    @Test
    void findByUsernameWithExistsCredential() {

        Optional<Credential> foundCredential = credentialRepository.findByUsername("giorgio.strada");

        assertTrue(foundCredential.isPresent());
        assertEquals("giorgio.strada", foundCredential.get().getUsername());
    }

    @Test
    void findByUsernameWithNotExistsCredential() {

        Optional<Credential> foundCredential = credentialRepository.findByUsername("andre.piccardi");

        assertFalse(foundCredential.isPresent());
    }
}