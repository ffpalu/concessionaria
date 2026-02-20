package com.ffpalu.concessionaria.middleware;

import com.ffpalu.concessionaria.dto.request.CustomerRequest;
import com.ffpalu.concessionaria.dto.response.CustomerResponse;
import com.ffpalu.concessionaria.entity.Customer;
import com.ffpalu.concessionaria.middleware.interfaces.CustomerMiddleware;
import com.ffpalu.concessionaria.service.interfaces.CustomerService;
import com.ffpalu.concessionaria.utils.Mapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class CustomerMiddlewareImpl implements CustomerMiddleware {

    private final CustomerService customerService;
    private final Mapper mapper;


    @Override
    public CustomerResponse createCustomer(CustomerRequest request) {
        Customer customerCreated =  customerService.createCustomer(request);
        return mapper.mapToCustomerResponse(customerCreated);
    }

    @Override
    public Page<CustomerResponse> getAllCustomer(Pageable pageable) {
        return customerService.getAllCustomer(pageable).map(mapper::mapToCustomerResponse);
    }

    @Override
    public Optional<CustomerResponse> getByCF(String CF) {
        return customerService.getCustomerByCF(CF).map(mapper::mapToCustomerResponse);
    }

    @Override
    public CustomerResponse updateCustomer(CustomerRequest customer, String ID) {
        Customer customerUpdated = customerService.updateCustomer(customer, UUID.fromString(ID));
        return mapper.mapToCustomerResponse(customerUpdated);
    }

    @Override
    public Page<CustomerResponse> getTopCustomer(Pageable pageable) {
        return customerService.getCustomerOrderedByNumSales(pageable).map(mapper::mapToCustomerResponse);
    }

    @Override
    public Page<CustomerResponse> getCustomerByNameAndSurname(String name, String surname, Pageable pageable) {
        return customerService.getCustomerByFirstNameAndLastName(name, surname, pageable).map(mapper::mapToCustomerResponse);
    }
}
