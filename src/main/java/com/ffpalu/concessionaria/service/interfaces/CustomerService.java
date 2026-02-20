package com.ffpalu.concessionaria.service.interfaces;

import com.ffpalu.concessionaria.dto.request.CustomerRequest;
import com.ffpalu.concessionaria.entity.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface CustomerService {


    Customer createCustomer(CustomerRequest customer);

    Customer updateCustomer(CustomerRequest customer, UUID id);

    Optional<Customer> getCustomerByCF(String CF);

    Page<Customer> getAllCustomer(Pageable pageable);

    Page<Customer> getCustomerOrderedByNumSales(Pageable pageable);

    Page<Customer> getCustomerByFirstNameAndLastName(String firstName, String lastName, Pageable pageable);

}
