package com.ffpalu.concessionaria.middleware.interfaces;

import com.ffpalu.concessionaria.dto.request.CustomerRequest;
import com.ffpalu.concessionaria.dto.response.CustomerResponse;
import com.ffpalu.concessionaria.entity.Customer;
import com.ffpalu.concessionaria.middleware.CustomerMiddlewareImpl;
import com.ffpalu.concessionaria.service.interfaces.CustomerService;
import com.ffpalu.concessionaria.utils.Mapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;


import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomerMiddlewareTest {
    @Mock
    private CustomerService customerService;
    @Mock private Mapper mapper;
    @InjectMocks
    private CustomerMiddlewareImpl customerMiddleware;

    private final Customer customer = Customer.builder()
            .id(UUID.randomUUID()).firstName("Luigi").lastName("Verdi")
            .CF("VRDLGU85B02F205X").email("luigi@test.com").build();

    private final CustomerResponse response = CustomerResponse.builder()
            .id(customer.getId()).firstName("Luigi").lastName("Verdi").build();

    @Test
    void createCustomerShouldDelegateAndMap() {
        CustomerRequest req = new CustomerRequest();
        when(customerService.createCustomer(req)).thenReturn(customer);
        when(mapper.mapToCustomerResponse(customer)).thenReturn(response);

        CustomerResponse result = customerMiddleware.createCustomer(req);

        assertEquals("Luigi", result.getFirstName());
    }

    @Test
    void getAllCustomerShouldReturnMappedPage() {
        Pageable pageable = PageRequest.of(0, 10);
        when(customerService.getAllCustomer(pageable)).thenReturn(new PageImpl<>(List.of(customer)));
        when(mapper.mapToCustomerResponse(customer)).thenReturn(response);

        Page<CustomerResponse> result = customerMiddleware.getAllCustomer(pageable);

        assertEquals(1, result.getTotalElements());
    }

    @Test
    void getByCFShouldReturnMappedOptional() {
        when(customerService.getCustomerByCF("VRDLGU85B02F205X")).thenReturn(Optional.of(customer));
        when(mapper.mapToCustomerResponse(customer)).thenReturn(response);

        Optional<CustomerResponse> result = customerMiddleware.getByCF("VRDLGU85B02F205X");

        assertTrue(result.isPresent());
    }

    @Test
    void updateCustomerShouldDelegateWithParsedUUID() {
        UUID id = UUID.randomUUID();
        CustomerRequest req = new CustomerRequest();
        when(customerService.updateCustomer(req, id)).thenReturn(customer);
        when(mapper.mapToCustomerResponse(customer)).thenReturn(response);

        CustomerResponse result = customerMiddleware.updateCustomer(req, id.toString());

        assertNotNull(result);
    }

    @Test
    void getTopCustomerShouldDelegate() {
        Pageable pageable = PageRequest.of(0, 10);
        when(customerService.getCustomerOrderedByNumSales(pageable)).thenReturn(new PageImpl<>(List.of(customer)));
        when(mapper.mapToCustomerResponse(customer)).thenReturn(response);

        Page<CustomerResponse> result = customerMiddleware.getTopCustomer(pageable);

        assertEquals(1, result.getTotalElements());
    }

    @Test
    void getCustomerByNameAndSurnameShouldDelegate() {
        Pageable pageable = PageRequest.of(0, 10);
        when(customerService.getCustomerByFirstNameAndLastName("Luigi", "Verdi", pageable))
                .thenReturn(new PageImpl<>(List.of(customer)));
        when(mapper.mapToCustomerResponse(customer)).thenReturn(response);

        Page<CustomerResponse> result = customerMiddleware.getCustomerByNameAndSurname("Luigi", "Verdi", pageable);

        assertEquals(1, result.getTotalElements());
    }
}