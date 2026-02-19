package com.ffpalu.concessionaria.controller.interfaces;

import com.ffpalu.concessionaria.dto.request.RegistrationCustomer;
import com.ffpalu.concessionaria.dto.response.CustomerResponse;
import com.ffpalu.concessionaria.dto.validation.CustomerValidation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@SecurityRequirement(name = "bearerAuth")
@RequestMapping("/api/customer")
public interface CustomerController {

    @PutMapping("/create")
    ResponseEntity<CustomerResponse> createCustomer(
            @RequestBody
            @Validated({CustomerValidation.Creation.class})
            RegistrationCustomer customer
            );

    @GetMapping("/")
    ResponseEntity<Page<CustomerResponse>> getAllCustomer(Pageable pageable);

    @GetMapping("/{CF}")
    ResponseEntity<CustomerResponse> getByCF(@PathVariable @NotBlank String CF);

    @PatchMapping("/{ID}")
    ResponseEntity<CustomerResponse> updateCustomer(
            @RequestBody
            @Validated(CustomerValidation.Modify.class)
            RegistrationCustomer customer,

            @PathVariable
            String ID
    );

    @GetMapping("/topcustomer")
    ResponseEntity<Page<CustomerResponse>> getTopCustomer(Pageable pageable);

    @GetMapping("/search")
    ResponseEntity<Page<CustomerResponse>> getCustomerByNameAndSurname(
            @RequestParam String name,
            @RequestParam String surname,
            @RequestParam(required = false) Pageable pageable
    );

}
