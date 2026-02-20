package com.ffpalu.concessionaria.middleware.interfaces;

import com.ffpalu.concessionaria.dto.request.CustomerRequest;
import com.ffpalu.concessionaria.dto.response.CustomerResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface CustomerMiddleware {

    CustomerResponse createCustomer(CustomerRequest request);

    Page<CustomerResponse> getAllCustomer(Pageable pageable);

    Optional<CustomerResponse> getByCF(String CF);

    CustomerResponse updateCustomer(CustomerRequest customer, String ID);

    Page<CustomerResponse> getTopCustomer(Pageable pageable);

    Page<CustomerResponse> getCustomerByNameAndSurname(String name, String surname, Pageable pageable);

}
