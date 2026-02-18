package com.ffpalu.concessionaria.service.interfaces;

import com.ffpalu.concessionaria.dto.request.RegistrationCustomer;
import com.ffpalu.concessionaria.dto.response.CustomerResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface CustomerService {

    CustomerResponse createCustomer(RegistrationCustomer customer);

    CustomerResponse updateCustomer(RegistrationCustomer customer, UUID id);

    CustomerResponse getByCF(String CF);

    Page<CustomerResponse> getAllCustomer(Pageable pageable);

    Page<CustomerResponse> getCustomerOrderedByNumSales(Pageable pageable);

    Page<CustomerResponse> getCustomerByFirstNameAndLastName(String firstName, String lastName, Pageable pageable);

}
