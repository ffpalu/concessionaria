package com.ffpalu.concessionaria.controller;

import com.ffpalu.concessionaria.controller.interfaces.UserController;
import com.ffpalu.concessionaria.dto.request.UserDetailsRequest;
import com.ffpalu.concessionaria.dto.response.UserDetailsResponse;
import com.ffpalu.concessionaria.middleware.interfaces.UserMiddleware;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UserControllerImpl implements UserController {

    private final UserMiddleware userMiddleware;

    @Override
    public ResponseEntity<Page<UserDetailsResponse>> getAllUsers(Pageable pageable) {
        Page<UserDetailsResponse> listOfUsers = userMiddleware.getAllUsers(pageable);

        return ResponseEntity.ok(listOfUsers);
    }

    @Override
    public ResponseEntity<Optional<UserDetailsResponse>> getByCF(String CF) {
        Optional<UserDetailsResponse> user = userMiddleware.getByCF(CF);
        return ResponseEntity.ok(user);
    }

    @Override
    public ResponseEntity<Page<UserDetailsResponse>> getByNameAndSurname(String name, String surname, Pageable pageable) {
        Page<UserDetailsResponse> users = userMiddleware.getByNameAndSurname(name,surname,pageable);
        return ResponseEntity.ok(users);
    }

    @Override
    public ResponseEntity<String> deactivateUser(String ID) {
        log.warn("DEACTIVATING USER with UUID to deactivate: {}", ID);
        userMiddleware.deactivateUser(ID);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("User deactivated successfully");
    }

    @Override
    public ResponseEntity<UserDetailsResponse> updateUser(UserDetailsRequest fieldToPatch, String ID) {

        UserDetailsResponse response = userMiddleware.updateUser(fieldToPatch,ID);

        return ResponseEntity.ok(response);
    }
}
