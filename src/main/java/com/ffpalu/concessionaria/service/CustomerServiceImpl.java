package com.ffpalu.concessionaria.service;

import com.ffpalu.concessionaria.dto.request.CustomerRequest;
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

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final Mapper mapper;
    private final CustomerRepository customerRepository;

    @Override
    public Customer createCustomer(CustomerRequest customer) {
        if (customerRepository.existsByCF(customer.getCF())) {
            throw new CustomerException("Customer already present");
        }

        Customer updaterCustomer = mapper.mapToCustomer(customer);

        return customerRepository.save(updaterCustomer);
    }

    @Override
    public Customer updateCustomer(CustomerRequest customer, UUID id) {
        Customer customerToUpdate = customerRepository.findById(id)
                .orElseThrow(() -> new CustomerException("User not found for updating"));

        Customer updatedCustomer = updateCustomerOrDefault(customerToUpdate, customer);

        return customerRepository.save(updatedCustomer);

    }

    @Override
    public Page<Customer> getAllCustomer(Pageable pageable) {
        return customerRepository.findAll(pageable);
    }

    @Override
    public Page<Customer> getCustomerOrderedByNumSales(Pageable pageable) {

        Pageable pageable1 = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by("salesCount").descending());

        return customerRepository.findAll(pageable1);

    }

    @Override
    public Optional<Customer> getCustomerByCF(String CF) {

        return customerRepository.findByCF(CF);
    }

    public Customer updateCustomerOrDefault(Customer customer, CustomerRequest updaterCustomer) {
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
    public Page<Customer> getCustomerByFirstNameAndLastName(String firstName, String lastName, Pageable pageable) {
        return customerRepository.findByFirstNameAndLastName(firstName, lastName, pageable);
    }
}
