package com.ffpalu.concessionaria.controller;

import com.ffpalu.concessionaria.controller.interfaces.CustomerController;
import com.ffpalu.concessionaria.dto.request.CustomerRequest;
import com.ffpalu.concessionaria.dto.response.CustomerResponse;
import com.ffpalu.concessionaria.middleware.interfaces.CustomerMiddleware;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
public class CustomerControllerImpl implements CustomerController {

    private final CustomerMiddleware customerMiddleware;

    @Override
    public ResponseEntity<CustomerResponse> createCustomer(CustomerRequest customer) {
        CustomerResponse customerCreated = customerMiddleware.createCustomer(customer);
        return ResponseEntity.status(HttpStatus.CREATED).body(customerCreated);
    }

    @Override
    public ResponseEntity<Page<CustomerResponse>> getAllCustomer(Pageable pageable) {
        Page<CustomerResponse> response = customerMiddleware.getAllCustomer(pageable);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<CustomerResponse> getByCF(String CF) {
        CustomerResponse customerResponse = customerMiddleware.getByCF(CF).orElseThrow();
        return ResponseEntity.ok(customerResponse);
    }

    @Override
    public ResponseEntity<CustomerResponse> updateCustomer(CustomerRequest customer, String ID) {
        CustomerResponse updatedCustomer = customerMiddleware.updateCustomer(customer, ID);
        return ResponseEntity.ok(updatedCustomer);
    }

    @Override
    public ResponseEntity<Page<CustomerResponse>> getTopCustomer(Pageable pageable) {
        Page<CustomerResponse> responsePage = customerMiddleware.getTopCustomer(pageable);
        return ResponseEntity.ok(responsePage);
    }

    @Override
    public ResponseEntity<Page<CustomerResponse>> getCustomerByNameAndSurname(String name, String surname, Pageable pageable) {
        Page<CustomerResponse> responsePage = customerMiddleware.getCustomerByNameAndSurname(name, surname, pageable);
        return ResponseEntity.ok(responsePage);
    }
}
