package com.ffpalu.concessionaria.controller;

import com.ffpalu.concessionaria.controller.interfaces.UserController;
import com.ffpalu.concessionaria.dto.request.RegistrationRequest;
import com.ffpalu.concessionaria.dto.response.UserResponse;
import com.ffpalu.concessionaria.service.interfaces.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UserControllerImpl implements UserController {

    private final UserService userService;

    @Override
    public ResponseEntity<Page<UserResponse>> getAllUsers(Pageable pageable) {
        Page<UserResponse> listOfUsers = userService.getAllUser(pageable);

        return ResponseEntity.ok(listOfUsers);
    }

    @Override
    public ResponseEntity<?> getByCF(String CF) {
        Optional<UserResponse> user = userService.getUserByCF(CF);
        if (user.isEmpty()) return ResponseEntity.ok("User not found");
        return ResponseEntity.ok(user.get());
    }

    @Override
    public ResponseEntity<Page<UserResponse>> getByNameAndSurname(String name, String surname, Pageable pageable) {
        Page<UserResponse> users = userService.getUserByNameAndSurname(name,surname,pageable);
        return ResponseEntity.ok(users);
    }

    @Override
    public ResponseEntity<String> deactivateUser(String ID) {
        log.warn("DEACTIVATING USER with UUID to deactivate: {}", ID);
        userService.deactivateUser(UUID.fromString(ID));



        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("User deactivated successfully");
    }

    @Override
    public ResponseEntity<UserResponse> updateUser(RegistrationRequest fieldToPatch, String ID) {

        UserResponse response = userService.updateUser(UUID.fromString(ID), fieldToPatch);

        return ResponseEntity.ok(response);
    }
}
