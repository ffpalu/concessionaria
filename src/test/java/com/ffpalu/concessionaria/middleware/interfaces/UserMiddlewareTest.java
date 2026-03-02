package com.ffpalu.concessionaria.middleware.interfaces;

import com.ffpalu.concessionaria.dto.request.UserDetailsRequest;
import com.ffpalu.concessionaria.dto.response.UserDetailsResponse;
import com.ffpalu.concessionaria.entity.User;
import com.ffpalu.concessionaria.middleware.UserMiddlewareImpl;
import com.ffpalu.concessionaria.service.interfaces.UserService;
import com.ffpalu.concessionaria.utils.Mapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserMiddlewareTest {


    @Mock
    private UserService userService;
    @Mock private Mapper mapper;
    @InjectMocks
    private UserMiddlewareImpl userMiddleware;

    private final User user = User.builder()
            .id(UUID.randomUUID()).firstName("Giorgio").lastName("Strada")
            .CF("SCPGRC02A25M452X").email("giorgio@test.com").build();

    private final UserDetailsResponse response = UserDetailsResponse.builder()
            .id(user.getId()).firstName("Giorgio").lastName("Strada").build();

    @Test
    void getAllUsersShouldReturnMappedPage() {
        Pageable pageable = PageRequest.of(0, 10);
        when(userService.getAllUser(pageable)).thenReturn(new PageImpl<>(List.of(user)));
        when(mapper.mapToUserResponse(user)).thenReturn(response);

        Page<UserDetailsResponse> result = userMiddleware.getAllUsers(pageable);

        assertEquals(1, result.getTotalElements());
    }

    @Test
    void getByCFShouldReturnMappedOptional() {
        when(userService.getUserByCF("SCPGRC02A25M452X")).thenReturn(Optional.of(user));
        when(mapper.mapToUserResponse(user)).thenReturn(response);

        Optional<UserDetailsResponse> result = userMiddleware.getByCF("SCPGRC02A25M452X");

        assertTrue(result.isPresent());
    }

    @Test
    void getByNameAndSurnameShouldDelegate() {
        Pageable pageable = PageRequest.of(0, 10);
        when(userService.getUserByNameAndSurname("Giorgio", "Strada", pageable))
                .thenReturn(new PageImpl<>(List.of(user)));
        when(mapper.mapToUserResponse(user)).thenReturn(response);

        Page<UserDetailsResponse> result = userMiddleware.getByNameAndSurname("Giorgio", "Strada", pageable);

        assertEquals(1, result.getTotalElements());
    }

    @Test
    void deactivateUserShouldParseUUIDAndDelegate() {
        UUID id = UUID.randomUUID();

        userMiddleware.deactivateUser(id.toString());

        verify(userService).deactivateUser(id);
    }

    @Test
    void updateUserShouldParseUUIDAndDelegateAndMap() {
        UUID id = user.getId();
        UserDetailsRequest req = new UserDetailsRequest();
        when(userService.updateUser(id, req)).thenReturn(user);
        when(mapper.mapToUserResponse(user)).thenReturn(response);

        UserDetailsResponse result = userMiddleware.updateUser(req, id.toString());

        assertEquals("Giorgio", result.getFirstName());
    }
}