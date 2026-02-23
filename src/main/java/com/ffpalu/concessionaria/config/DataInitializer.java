package com.ffpalu.concessionaria.config;

import com.ffpalu.concessionaria.entity.*;
import com.ffpalu.concessionaria.entity.enums.Role;
import com.ffpalu.concessionaria.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.Year;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Profile("DEV")
public class DataInitializer implements CommandLineRunner {


    private final CredentialRepository credentialRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final SellerRepository sellerRepository;
    private final CustomerRepository customerRepository;
    private final VehicleRepository vehicleRepository;
    private final SaleRepository saleRepository;

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

        Seller seller1 = Seller.builder()
                .user(seller)
                .employeeCode("452s3a")
                .hireDate(LocalDate.now())
                .businessPhone("3451196897")
                .build();

        sellerRepository.save(seller1);


        Customer customer = Customer.builder()
                .firstName("giorgio")
                .lastName("andrea")
                .email("andy@email.com")
                .CF("PLMFCX85SD23A456Z")
                .build();

        customerRepository.save(customer);


        Vehicle mito = Vehicle.builder()
                .plate("ED570DB")
                .brand("Alfaromeo")
                .model("Mito")
                .used(true)
                .year(Year.of(2010))
                .numberOfKilometers(180000)
                .build();

        vehicleRepository.save(mito);


        Sale sale = Sale.builder()
                .sellDate(LocalDate.now())
                .price(3750.50)
                .sellerId(seller1.getId())
                .customerId(customer.getId())
                .vehicleId(mito.getId())
                .build();

        saleRepository.save(sale);

    }
}
