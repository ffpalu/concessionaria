package com.ffpalu.concessionaria.repository;

import com.ffpalu.concessionaria.entity.Customer;
import com.ffpalu.concessionaria.entity.User;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class CustomerRepositoryTest {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private TestEntityManager entityManager;

    @BeforeEach
    void setUp() {
        Customer customer = Customer.builder()
                .firstName("Giorgio").lastName("Scarpa")
                .email("giorgio@pamigi.it")
                .CF("GRISAD45Z12X365L")
                .build();

        entityManager.persistAndFlush(customer);
    }

    @Test
    void findByCFShouldReturnCustomerWhenPresent() {
        Optional<Customer> found = customerRepository.findByCF("GRISAD45Z12X365L");

        assertTrue(found.isPresent());
        assertEquals("GRISAD45Z12X365L", found.get().getCF());
        assertEquals("Giorgio", found.get().getFirstName());
        assertEquals("Scarpa", found.get().getLastName());
        assertEquals("giorgio@pamigi.it", found.get().getEmail());

    }

    @Test
    void findByCFShouldReturnNullWhenNotPresent() {
        Optional<Customer> found = customerRepository.findByCF("PLMFRC01T17L219P");

        assertFalse(found.isPresent());
    }


    @Test
    void findByFirstNameAndLastNameShouldReturnCustomersWhenPresent() {
        Page<Customer> customers = customerRepository.findByFirstNameAndLastName("Giorgio", "Scarpa", null);

        assertTrue(customers.hasContent());

        assertEquals("Giorgio", customers.getContent().get(0).getFirstName());
        assertEquals("Scarpa", customers.getContent().get(0).getLastName());
        assertEquals("GRISAD45Z12X365L",  customers.getContent().get(0).getCF());
        assertEquals("giorgio@pamigi.it", customers.getContent().get(0).getEmail());
    }

    @Test
    void findByFirstNameAndLastNameShouldReturnPageOfNullWhenNotPresent() {
        Page<Customer> customers = customerRepository.findByFirstNameAndLastName("Giorgio", "Andrea", null);

        assertFalse(customers.hasContent());
    }
}