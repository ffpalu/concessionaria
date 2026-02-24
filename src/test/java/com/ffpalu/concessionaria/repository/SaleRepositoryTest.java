package com.ffpalu.concessionaria.repository;

import com.ffpalu.concessionaria.entity.*;
import com.ffpalu.concessionaria.entity.enums.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.time.Year;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class SaleRepositoryTest {


    @Autowired
    private SaleRepository saleRepository;

    @Autowired
    private TestEntityManager entityManager;

    private Seller seller;
    private Customer customer;
    private Vehicle vehicle;
    private Sale sale;

    @BeforeEach
    void setUp() {
        User user = User.builder()
                .firstName("Mario")
                .lastName("Rossi")
                .CF("RSSMRA90A01H501Z")
                .email("mario@test.com")
                .build();
        entityManager.persistAndFlush(user);

        seller = Seller.builder()
                .user(user)
                .employeeCode("EMP001")
                .hireDate(LocalDate.of(2020, 1, 1))
                .businessPhone("3331234567")
                .build();
        entityManager.persistAndFlush(seller);

        customer = Customer.builder()
                .firstName("Luigi")
                .lastName("Verdi")
                .CF("VRDLGU85B02F205X")
                .email("luigi@test.com")
                .build();
        entityManager.persistAndFlush(customer);

        vehicle = Vehicle.builder()
                .plate("AB123CD")
                .brand("Fiat")
                .model("Panda")
                .used(false)
                .year(Year.of(2023))
                .numberOfKilometers(0)
                .build();
        entityManager.persistAndFlush(vehicle);

        sale = Sale.builder()
                .sellDate(LocalDate.of(2025, 6, 15))
                .price(15000.0)
                .sellerId(seller.getId())
                .customerId(customer.getId())
                .vehicleId(vehicle.getId())
                .build();
        entityManager.persistAndFlush(sale);

        entityManager.clear();


    }

    @Test
    void existsByVehicleIdShouldReturnTrueWhenSaleExistsForVehicle() {
        boolean exists = saleRepository.existsByVehicleId(vehicle.getId());
        assertTrue(exists);
    }

    @Test
    void existsByVehicleIdShouldReturnFalseWhenNoSaleExists() {
        boolean exists = saleRepository.existsByVehicleId(UUID.randomUUID());
        assertFalse(exists);
    }

    @Test
    void findBySellerIdShouldReturnSalesWhenSellersHasSales() {
        Page<Sale> sales = saleRepository.findBySellerId(seller.getId(), PageRequest.of(0,10));

        assertFalse(sales.isEmpty());
        assertEquals(1, sales.getTotalElements());
    }

    @Test
    void findBySellerIdShouldReturnEmptyWhenSellerHasNoSale() {
        User user2 = User.builder()
                .firstName("Paolo")
                .lastName("Bonolis")
                .CF("BNCPLA80C03G702Y")
                .email("paolo@test.com")
                .build();
        entityManager.persistAndFlush(user2);

        Seller sellerWithoutSales = Seller.builder()
                .user(user2)
                .employeeCode("EMP002")
                .hireDate(LocalDate.now())
                .build();

        Page<Sale> sales = saleRepository.findBySellerId(sellerWithoutSales.getId(), PageRequest.of(0,10));
        assertTrue(sales.isEmpty());
    }

    @Test
    void findBySellerIdShouldReturnSalesWhenSellersHasManySale() {
        Vehicle mito = Vehicle.builder()
                .plate("ED570DB")
                .brand("Alfaromeo")
                .model("Mito")
                .used(true)
                .year(Year.of(2010))
                .numberOfKilometers(180000)
                .build();
        entityManager.persistAndFlush(mito);

        /*Sale sale2 = Sale.builder()
                .sellDate(LocalDate.of(2025,7,20))
                .price(3000.0)
                .*/

    }
}