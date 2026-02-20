package com.ffpalu.concessionaria.controller.interfaces;

import com.ffpalu.concessionaria.dto.request.UserDetailsRequest;
import com.ffpalu.concessionaria.dto.response.UserDetailsResponse;
import com.ffpalu.concessionaria.dto.validation.UserValidationGroup;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RequestMapping("api/user")
@SecurityRequirement(name = "bearerAuth")
public interface UserController {

    @GetMapping("/")
    ResponseEntity<Page<UserDetailsResponse>> getAllUsers(Pageable pageable);

    @GetMapping("/{CF}")
    ResponseEntity<?> getByCF(@PathVariable @NotBlank String CF);

    @GetMapping("/search")
    ResponseEntity<Page<UserDetailsResponse>> getByNameAndSurname(
            @RequestParam String name,
            @RequestParam String surname,
            Pageable pageable
    );

    @PatchMapping("/{ID}")
    @PreAuthorize("hasAnyRole('ROLE_SUPPORT', 'ROLE_ADMIN')")
    ResponseEntity<UserDetailsResponse> updateUser(
            @RequestBody @Validated(UserValidationGroup.Modify.class) UserDetailsRequest fieldToPatch,
            @PathVariable String ID
            );

    @DeleteMapping("/deactivate/{ID}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    ResponseEntity<String> deactivateUser(@PathVariable String ID);
}
