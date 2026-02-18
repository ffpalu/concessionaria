package com.ffpalu.concessionaria.controller;

import com.ffpalu.concessionaria.controller.interfaces.CustomerController;
import com.ffpalu.concessionaria.dto.request.RegistrationCustomer;
import com.ffpalu.concessionaria.dto.response.CustomerResponse;
import com.ffpalu.concessionaria.service.interfaces.CustomerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
public class CustomerControllerImpl implements CustomerController {

    private final CustomerService customerService;

    @Override
    public ResponseEntity<CustomerResponse> createCustomer(RegistrationCustomer customer) {
        CustomerResponse customerCreated = customerService.createCustomer(customer);
        return ResponseEntity.status(201).body(customerCreated);
    }

    @Override
    public ResponseEntity<Page<CustomerResponse>> getAllCustomer(Pageable pageable) {
        Page<CustomerResponse> response = customerService.getAllCustomer(pageable);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<CustomerResponse> getByCF(String CF) {
        CustomerResponse customerResponse = customerService.getByCF(CF);
        return ResponseEntity.ok(customerResponse);
    }

    @Override
    public ResponseEntity<CustomerResponse> updateCustomer(RegistrationCustomer customer, String ID) {
        log.info("converting {} to uuid {}", ID, UUID.fromString(ID));
        CustomerResponse updatedCustomer = customerService.updateCustomer(customer, UUID.fromString(ID));
        return ResponseEntity.ok(updatedCustomer);
    }

    @Override
    public ResponseEntity<Page<CustomerResponse>> getTopCustomer(Pageable pageable) {
        Page<CustomerResponse> responsePage = customerService.getCustomerOrderedByNumSales(pageable);
        return ResponseEntity.ok(responsePage);
    }

    @Override
    public ResponseEntity<Page<CustomerResponse>> getCustomerByNameAndSurname(String name, String surname, Pageable pageable) {
        Page<CustomerResponse> responsePage = customerService.getCustomerByFirstNameAndLastName(name, surname, pageable);
        return ResponseEntity.ok(responsePage);
    }
}
