package com.ffpalu.concessionaria.controller.interfaces;

import com.ffpalu.concessionaria.dto.request.RegistrationRequest;
import com.ffpalu.concessionaria.dto.response.UserResponse;
import com.ffpalu.concessionaria.dto.validation.UserValidationGroup;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequestMapping("api/user")
public interface UserController {

    @GetMapping("/")
    ResponseEntity<Page<UserResponse>> getAllUsers(Pageable pageable);

    @GetMapping("/{CF}")
    ResponseEntity<?> getByCF(@PathVariable @NotBlank String CF);

    @GetMapping("/search")
    ResponseEntity<Page<UserResponse>> getByNameAndSurname(
            @RequestParam String name,
            @RequestParam String surname,
            Pageable pageable
    );

    @PatchMapping("/{ID}")
    @PreAuthorize("hasAnyRole('ROLE_SUPPORT', 'ROLE_ADMIN')")
    ResponseEntity<UserResponse> updateUser(
            @RequestBody @Validated(UserValidationGroup.Modify.class) RegistrationRequest fieldToPatch,
            @PathVariable String ID
            );

    @DeleteMapping("/deactivate/{ID}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    ResponseEntity<String> deactivateUser(@PathVariable String ID);
}
