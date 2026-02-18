package com.ffpalu.concessionaria.service;

import com.ffpalu.concessionaria.dto.request.RegistrationCustomer;
import com.ffpalu.concessionaria.dto.response.CustomerResponse;
import com.ffpalu.concessionaria.entity.Customer;
import com.ffpalu.concessionaria.exceptions.CustomerException;
import com.ffpalu.concessionaria.repository.CustomerRepository;
import com.ffpalu.concessionaria.service.interfaces.CustomerService;
import com.ffpalu.concessionaria.utils.Mapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final Mapper mapper;
    private final CustomerRepository customerRepository;

    @Override
    public CustomerResponse createCustomer(RegistrationCustomer customer) {
        if(customerRepository.existsByCF(customer.getCF())) {
            throw new CustomerException("Customer already present");
        }

        Customer customerCreated = customerRepository.save(mapper.mapToCustomer(customer));

        return mapper.mapToCustomerResponse(customerCreated);
    }

    @Override
    public CustomerResponse updateCustomer(RegistrationCustomer customer, UUID id) {
        Customer customerToUpdate = customerRepository.findById(id)
                .orElseThrow(() -> new CustomerException("User not found for updating"));

        Customer updatedCustomer = updateCustomerOrDefault(customerToUpdate, customer);

        updatedCustomer = customerRepository.save(updatedCustomer);

        return mapper.mapToCustomerResponse(updatedCustomer);

    }

    @Override
    public Page<CustomerResponse> getAllCustomer(Pageable pageable) {
        return customerRepository.findAll(pageable).map(mapper::mapToCustomerResponse);
    }

    @Override
    public Page<CustomerResponse> getCustomerOrderedByNumSales(Pageable pageable) {

        Pageable pageable1 = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by("salesCount").descending());

        return customerRepository.findAll(pageable1).map(mapper::mapToCustomerResponse);

    }

    @Override
    public CustomerResponse getByCF(String CF) {
        Customer findedCustomer = customerRepository.findByCF(CF).orElseThrow(() -> new CustomerException("User with CF " + CF + " not found"));

        return mapper.mapToCustomerResponse(findedCustomer);
    }

    public Customer updateCustomerOrDefault(Customer customer, RegistrationCustomer updaterCustomer) {
        return Customer.builder()
                .id(customer.getId())
                .email(updaterCustomer.getEmail() == null ? customer.getEmail() : updaterCustomer.getEmail())
                .CF(updaterCustomer.getCF() == null ? customer.getCF() : updaterCustomer.getCF())
                .firstName(updaterCustomer.getFirstName() == null ? customer.getFirstName() : updaterCustomer.getFirstName())
                .lastName(updaterCustomer.getLastName() == null ? customer.getLastName() : updaterCustomer.getLastName())
                .sales(customer.getSales())
                .build();
    }

    @Override
    public Page<CustomerResponse> getCustomerByFirstNameAndLastName(String firstName, String lastName, Pageable pageable) {
        return customerRepository.findByFirstNameAndLastName(firstName, lastName, pageable)
                .map(mapper::mapToCustomerResponse);
    }
}
