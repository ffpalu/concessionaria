package com.ffpalu.concessionaria.service.interfaces;

import com.ffpalu.concessionaria.dto.request.UserDetailsRequest;
import com.ffpalu.concessionaria.entity.Credential;
import com.ffpalu.concessionaria.entity.User;
import com.ffpalu.concessionaria.exceptions.UserException;
import com.ffpalu.concessionaria.repository.UserRepository;
import com.ffpalu.concessionaria.service.UserServiceImpl;
import com.ffpalu.concessionaria.utils.Mapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private Mapper mapper;

    @InjectMocks
    private UserServiceImpl userService;

    private User user;
    private UserDetailsRequest userRequest;
    private UUID userId;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();

        user = User.builder()
                .id(userId)
                .firstName("Giorgio")
                .lastName("Strada")
                .CF("SCPGRC02A25M452X")
                .email("giorgio@test.com")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        userRequest = new UserDetailsRequest();
        userRequest.setFirstName("Giorgio");
        userRequest.setLastName("Strada");
        userRequest.setCF("SCPGRC02A25M452X");
        userRequest.setEmail("giorgio@test.com");
    }


    @Test
    void getUserByUsernameShouldReturnUserWhenExists() {
        when(userRepository.getUserByUsername("giorgio.strada")).thenReturn(Optional.of(user));

        Optional<User> result = userService.getUserByUsername("giorgio.strada");

        assertTrue(result.isPresent());
        assertEquals("Giorgio", result.get().getFirstName());
    }

    @Test
    void getUserByUsernameShouldReturnEmptyWhenNotExists() {
        when(userRepository.getUserByUsername("nonexistent")).thenReturn(Optional.empty());

        Optional<User> result = userService.getUserByUsername("nonexistent");

        assertTrue(result.isEmpty());
    }


    @Test
    void checkIfUserExistsShouldReturnTrueWhenCFOrEmailMatch() {
        when(userRepository.existsByCFOrEmail("SCPGRC02A25M452X", "giorgio@test.com")).thenReturn(true);

        assertTrue(userService.checkIfUserExists("SCPGRC02A25M452X", "giorgio@test.com"));
    }

    @Test
    void checkIfUserExistsShouldReturnFalseWhenNoMatch() {
        when(userRepository.existsByCFOrEmail("NONEXISTENT", "no@test.com")).thenReturn(false);

        assertFalse(userService.checkIfUserExists("NONEXISTENT", "no@test.com"));
    }


    @Test
    void createUserShouldSaveAndReturnWhenUserNotExists() {
        when(userRepository.existsByCFOrEmail("SCPGRC02A25M452X", "giorgio@test.com")).thenReturn(false);
        when(mapper.mapToUser(userRequest)).thenReturn(user);
        when(userRepository.save(user)).thenReturn(user);

        User result = userService.createUser(userRequest);

        assertNotNull(result);
        assertEquals("Giorgio", result.getFirstName());
        verify(userRepository).existsByCFOrEmail("SCPGRC02A25M452X", "giorgio@test.com");
        verify(mapper).mapToUser(userRequest);
        verify(userRepository).save(user);
    }

    @Test
    void createUserShouldThrowWhenUserAlreadyExists() {
        when(userRepository.existsByCFOrEmail("SCPGRC02A25M452X", "giorgio@test.com")).thenReturn(true);

        assertThrows(UserException.class, () -> userService.createUser(userRequest));
        verify(userRepository, never()).save(any());
        verify(mapper, never()).mapToUser(any());
    }

    @Test
    void getUserByCFShouldReturnUserWhenExists() {
        when(userRepository.findUserByCF("SCPGRC02A25M452X")).thenReturn(Optional.of(user));

        Optional<User> result = userService.getUserByCF("SCPGRC02A25M452X");

        assertTrue(result.isPresent());
        assertEquals("SCPGRC02A25M452X", result.get().getCF());
    }

    @Test
    void getUserByCFShouldReturnEmptyWhenNotExists() {
        when(userRepository.findUserByCF("NONEXISTENT")).thenReturn(Optional.empty());

        Optional<User> result = userService.getUserByCF("NONEXISTENT");

        assertTrue(result.isEmpty());
    }


    @Test
    void getUserByNameAndSurnameShouldReturnMatchingUsers() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<User> page = new PageImpl<>(List.of(user));
        when(userRepository.findByFirstNameAndLastName("Giorgio", "Strada", pageable)).thenReturn(page);

        Page<User> result = userService.getUserByNameAndSurname("Giorgio", "Strada", pageable);

        assertEquals(1, result.getTotalElements());
    }

    @Test
    void getUserByNameAndSurnameShouldReturnEmptyWhenNoMatch() {
        Pageable pageable = PageRequest.of(0, 10);
        when(userRepository.findByFirstNameAndLastName("Nobody", "Here", pageable))
                .thenReturn(Page.empty(pageable));

        Page<User> result = userService.getUserByNameAndSurname("Nobody", "Here", pageable);

        assertTrue(result.isEmpty());
    }


    @Test
    void getAllUserShouldReturnAllUsers() {
        Pageable pageable = PageRequest.of(0, 10);
        User user2 = User.builder()
                .id(UUID.randomUUID())
                .firstName("Claudio")
                .lastName("Piccardi")
                .CF("PCDCLD75A12D969K")
                .email("claudio@test.com")
                .build();

        Page<User> page = new PageImpl<>(List.of(user, user2));
        when(userRepository.findAll(pageable)).thenReturn(page);

        Page<User> result = userService.getAllUser(pageable);

        assertEquals(2, result.getTotalElements());
    }


    @Test
    void deactivateUserShouldDeactivateAndSaveWhenUserExists() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);

        userService.deactivateUser(userId);

        assertFalse(user.isActive());
        verify(userRepository).findById(userId);
        verify(userRepository).save(user);
    }

    @Test
    void deactivateUserShouldThrowWhenUserNotFound() {
        UUID nonExistentId = UUID.randomUUID();
        when(userRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        assertThrows(UserException.class, () -> userService.deactivateUser(nonExistentId));
        verify(userRepository, never()).save(any());
    }


    @Test
    void updateUserShouldUpdateAllFieldsWhenAllProvided() {
        UserDetailsRequest updateRequest = new UserDetailsRequest();
        updateRequest.setFirstName("Marco");
        updateRequest.setLastName("Neri");
        updateRequest.setCF("NREMRC90C03L219Y");
        updateRequest.setEmail("marco@test.com");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

        User result = userService.updateUser(userId, updateRequest);

        assertEquals("Marco", result.getFirstName());
        assertEquals("Neri", result.getLastName());
        assertEquals("NREMRC90C03L219Y", result.getCF());
        assertEquals("marco@test.com", result.getEmail());
        verify(userRepository).save(user);
    }

    @Test
    void updateUserShouldUpdateOnlyNonNullFields() {
        UserDetailsRequest partialUpdate = new UserDetailsRequest();
        partialUpdate.setFirstName(null);
        partialUpdate.setLastName(null);
        partialUpdate.setCF(null);
        partialUpdate.setEmail("newemail@test.com");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

        User result = userService.updateUser(userId, partialUpdate);

        assertEquals("Giorgio", result.getFirstName());
        assertEquals("Strada", result.getLastName());
        assertEquals("SCPGRC02A25M452X", result.getCF());
        assertEquals("newemail@test.com", result.getEmail());
        verify(userRepository).save(user);
    }

    @Test
    void updateUserShouldThrowWhenUserNotFound() {
        UUID nonExistentId = UUID.randomUUID();
        when(userRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        assertThrows(UserException.class, () -> userService.updateUser(nonExistentId, userRequest));
        verify(userRepository, never()).save(any());
    }

    @Test
    void updateUserShouldPreserveIdAfterReflectionUpdate() {
        UserDetailsRequest updateRequest = new UserDetailsRequest();
        updateRequest.setFirstName("NuovoNome");
        updateRequest.setLastName(null);
        updateRequest.setCF(null);
        updateRequest.setEmail(null);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

        User result = userService.updateUser(userId, updateRequest);

        assertEquals(userId, result.getId());
        assertEquals("NuovoNome", result.getFirstName());
    }
}