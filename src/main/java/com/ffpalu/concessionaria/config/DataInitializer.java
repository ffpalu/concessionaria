package com.ffpalu.concessionaria.config;

import com.ffpalu.concessionaria.entity.Credential;
import com.ffpalu.concessionaria.entity.User;
import com.ffpalu.concessionaria.entity.enums.Role;
import com.ffpalu.concessionaria.repository.CredentialRepository;
import com.ffpalu.concessionaria.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Profile("DEV")
public class DataInitializer implements CommandLineRunner {


    private final CredentialRepository credentialRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) throws Exception {


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
}
