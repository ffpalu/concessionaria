package com.ffpalu.concessionaria.service.interfaces;

import com.ffpalu.concessionaria.dto.request.CustomerRequest;
import com.ffpalu.concessionaria.entity.Customer;
import com.ffpalu.concessionaria.exceptions.CustomerException;
import com.ffpalu.concessionaria.repository.CustomerRepository;
import com.ffpalu.concessionaria.service.CustomerServiceImpl;
import com.ffpalu.concessionaria.utils.Mapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private Mapper mapper;

    @InjectMocks
    private CustomerServiceImpl customerService;

    private Customer customer;
    private CustomerRequest customerRequest;
    private UUID customerId;

    @BeforeEach
    void setUp() {
        customerId = UUID.randomUUID();

        customer = Customer.builder()
                .id(customerId)
                .firstName("Luigi")
                .lastName("Verdi")
                .email("luigi@test.com")
                .CF("VRDLGU85B02F205X")
                .build();

        customerRequest = new CustomerRequest();
        customerRequest.setFirstName("Luigi");
        customerRequest.setLastName("Verdi");
        customerRequest.setEmail("luigi@test.com");
        customerRequest.setCF("VRDLGU85B02F205X");
    }


    @Test
    void createCustomerShouldSaveAndReturnWhenCFNotExists() {
        when(customerRepository.existsByCF("VRDLGU85B02F205X")).thenReturn(false);
        when(mapper.mapToCustomer(customerRequest)).thenReturn(customer);
        when(customerRepository.save(customer)).thenReturn(customer);

        Customer result = customerService.createCustomer(customerRequest);

        assertNotNull(result);
        assertEquals("Luigi", result.getFirstName());
        assertEquals("VRDLGU85B02F205X", result.getCF());
        verify(customerRepository).existsByCF("VRDLGU85B02F205X");
        verify(mapper).mapToCustomer(customerRequest);
        verify(customerRepository).save(customer);
    }

    @Test
    void createCustomerShouldThrowWhenCFAlreadyExists() {
        when(customerRepository.existsByCF("VRDLGU85B02F205X")).thenReturn(true);

        assertThrows(CustomerException.class, () -> customerService.createCustomer(customerRequest));
        verify(customerRepository, never()).save(any());
        verify(mapper, never()).mapToCustomer(any());
    }


    @Test
    void updateCustomerShouldUpdateAllFieldsWhenAllProvided() {
        CustomerRequest updateRequest = new CustomerRequest();
        updateRequest.setFirstName("Marco");
        updateRequest.setLastName("Neri");
        updateRequest.setEmail("marco@test.com");
        updateRequest.setCF("NREMRC90C03L219Y");

        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
        when(customerRepository.save(any(Customer.class))).thenAnswer(inv -> inv.getArgument(0));

        Customer result = customerService.updateCustomer(updateRequest, customerId);

        assertEquals("Marco", result.getFirstName());
        assertEquals("Neri", result.getLastName());
        assertEquals("marco@test.com", result.getEmail());
        assertEquals("NREMRC90C03L219Y", result.getCF());
        assertEquals(customerId, result.getId());
        verify(customerRepository).save(any(Customer.class));
    }

    @Test
    void updateCustomerShouldKeepOldValuesWhenFieldsAreNull() {
        CustomerRequest partialUpdate = new CustomerRequest();
        partialUpdate.setFirstName(null);
        partialUpdate.setLastName(null);
        partialUpdate.setEmail("newemail@test.com");
        partialUpdate.setCF(null);

        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
        when(customerRepository.save(any(Customer.class))).thenAnswer(inv -> inv.getArgument(0));

        Customer result = customerService.updateCustomer(partialUpdate, customerId);

        assertEquals("Luigi", result.getFirstName());
        assertEquals("Verdi", result.getLastName());
        assertEquals("newemail@test.com", result.getEmail());
        assertEquals("VRDLGU85B02F205X", result.getCF());
    }

    @Test
    void updateCustomerShouldThrowWhenCustomerNotFound() {
        UUID nonExistentId = UUID.randomUUID();
        when(customerRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        assertThrows(CustomerException.class, () -> customerService.updateCustomer(customerRequest, nonExistentId));
        verify(customerRepository, never()).save(any());
    }


    @Test
    void getAllCustomerShouldReturnPage() {
        org.springframework.data.domain.Pageable pageable = PageRequest.of(0, 10);
        Page<Customer> page = new PageImpl<>(List.of(customer));
        when(customerRepository.findAll(pageable)).thenReturn(page);

        Page<Customer> result = customerService.getAllCustomer(pageable);

        assertEquals(1, result.getTotalElements());
        assertEquals("Luigi", result.getContent().get(0).getFirstName());
    }

    @Test
    void getAllCustomerShouldReturnEmptyPageWhenNoCustomers() {
        Pageable pageable = PageRequest.of(0, 10);
        when(customerRepository.findAll(pageable)).thenReturn(Page.empty(pageable));

        Page<Customer> result = customerService.getAllCustomer(pageable);

        assertTrue(result.isEmpty());
    }


    @Test
    void getCustomerOrderedByNumSalesShouldApplyDescendingSortBySalesCount() {
        Pageable inputPageable = PageRequest.of(0, 10);
        Pageable expectedPageable = PageRequest.of(0, 10, Sort.by("salesCount").descending());

        Page<Customer> page = new PageImpl<>(List.of(customer));
        when(customerRepository.findAll(expectedPageable)).thenReturn(page);

        Page<Customer> result = customerService.getCustomerOrderedByNumSales(inputPageable);

        assertEquals(1, result.getTotalElements());
        verify(customerRepository).findAll(expectedPageable);
    }


    @Test
    void getCustomerByCFShouldReturnCustomerWhenExists() {
        when(customerRepository.findByCF("VRDLGU85B02F205X")).thenReturn(Optional.of(customer));

        Optional<Customer> result = customerService.getCustomerByCF("VRDLGU85B02F205X");

        assertTrue(result.isPresent());
        assertEquals("Luigi", result.get().getFirstName());
    }

    @Test
    void getCustomerByCFShouldReturnEmptyWhenNotExists() {
        when(customerRepository.findByCF("NONEXISTENT")).thenReturn(Optional.empty());

        Optional<Customer> result = customerService.getCustomerByCF("NONEXISTENT");

        assertTrue(result.isEmpty());
    }


    @Test
    void getCustomerByFirstNameAndLastNameShouldReturnMatchingCustomers() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Customer> page = new PageImpl<>(List.of(customer));
        when(customerRepository.findByFirstNameAndLastName("Luigi", "Verdi", pageable)).thenReturn(page);

        Page<Customer> result = customerService.getCustomerByFirstNameAndLastName("Luigi", "Verdi", pageable);

        assertEquals(1, result.getTotalElements());
        assertEquals("Luigi", result.getContent().get(0).getFirstName());
    }

    @Test
    void getCustomerByFirstNameAndLastNameShouldReturnEmptyWhenNoMatch() {
        Pageable pageable = PageRequest.of(0, 10);
        when(customerRepository.findByFirstNameAndLastName("Nobody", "Here", pageable)).thenReturn(Page.empty(pageable));

        Page<Customer> result = customerService.getCustomerByFirstNameAndLastName("Nobody", "Here", pageable);

        assertTrue(result.isEmpty());
    }
}