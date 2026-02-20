package com.ffpalu.concessionaria.service.interfaces;

import com.ffpalu.concessionaria.dto.request.RegistrationSellerDetailsRequest;
import com.ffpalu.concessionaria.dto.response.CustomerResponse;
import com.ffpalu.concessionaria.entity.Seller;
import com.ffpalu.concessionaria.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.swing.text.html.Option;
import java.util.Optional;
import java.util.UUID;

public interface SellerService {

    boolean checkIfPresent(String employeeCode);

    Seller createSeller(RegistrationSellerDetailsRequest request, User user);

    Page<Seller> getSellerByOrderedByNumSales(Pageable pageable);

    Optional<Seller> findByUsername(String username);

    Optional<Seller> findById(UUID id);

}
