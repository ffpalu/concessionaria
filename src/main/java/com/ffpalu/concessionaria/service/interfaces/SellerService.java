package com.ffpalu.concessionaria.service.interfaces;

import com.ffpalu.concessionaria.dto.request.SellerDetailsRequest;
import com.ffpalu.concessionaria.entity.Seller;
import com.ffpalu.concessionaria.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface SellerService {

    boolean checkIfPresent(String employeeCode);

    Seller createSeller(SellerDetailsRequest request, User user);

    Page<Seller> getSellerByOrderedByNumSales(Pageable pageable);

    Optional<Seller> findByUsername(String username);

    Optional<Seller> findById(UUID id);

}
