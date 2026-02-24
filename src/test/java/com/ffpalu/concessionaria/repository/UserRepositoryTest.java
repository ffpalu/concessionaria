package com.ffpalu.concessionaria.repository;

import com.ffpalu.concessionaria.entity.User;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestEntityManager entityManager;


    @BeforeEach
    void setUp() {
        User user = User.builder()
                .firstName("Mario").lastName("Rossi")
                .CF("MRCSHA45S42Z658A").email("mario@test.com")
                .build();

        entityManager.persistAndFlush(user);
    }

    @Test
    void findUserByCFShouldReturnUserWhenExists() {
        Optional<User> found = userRepository.findUserByCF("MRCSHA45S42Z658A");

        assertTrue(found.isPresent());
        assertEquals("Mario", found.get().getFirstName());
        assertEquals("Rossi", found.get().getLastName());
        assertEquals("MRCSHA45S42Z658A", found.get().getCF());
        assertEquals("mario@test.com", found.get().getEmail());

    }

    @Test
    void findUserByCFShouldReturnEmptyWhenNoUserExists() {
        Optional<User> found = userRepository.findUserByCF("PLMFRC01T17L219P");

        assertFalse(found.isPresent());

    }
}