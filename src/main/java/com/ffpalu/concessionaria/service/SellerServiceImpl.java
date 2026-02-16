package com.ffpalu.concessionaria.service;

import com.ffpalu.concessionaria.dto.request.RegistrationSellerDetailsRequest;
import com.ffpalu.concessionaria.entity.Seller;
import com.ffpalu.concessionaria.entity.User;
import com.ffpalu.concessionaria.exceptions.SellerException;
import com.ffpalu.concessionaria.repository.SellerRepository;
import com.ffpalu.concessionaria.service.interfaces.SellerService;
import com.ffpalu.concessionaria.utils.Mapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SellerServiceImpl implements SellerService {

    private final SellerRepository sellerRepository;
    private final Mapper mapper;

    @Override
    public boolean checkIfPresent(String employeeCode) {
        return sellerRepository.existsByEmployeeCode(employeeCode);
    }

    @Override
    public Seller createSeller(RegistrationSellerDetailsRequest request, User user) {
        if (checkIfPresent(request.getEmployeeCode())) {
            throw new SellerException("Seller already exists");
        }

        Seller seller = mapper.mapToSeller(request, user);

        return sellerRepository.save(seller);

    }
}
