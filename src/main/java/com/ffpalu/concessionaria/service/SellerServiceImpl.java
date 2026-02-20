package com.ffpalu.concessionaria.service;

import com.ffpalu.concessionaria.dto.request.SellerDetailsRequest;
import com.ffpalu.concessionaria.entity.Seller;
import com.ffpalu.concessionaria.entity.User;
import com.ffpalu.concessionaria.exceptions.SellerException;
import com.ffpalu.concessionaria.repository.SellerRepository;
import com.ffpalu.concessionaria.service.interfaces.SellerService;
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
public class SellerServiceImpl implements SellerService {

    private final SellerRepository sellerRepository;
    private final Mapper mapper;

    @Override
    public boolean checkIfPresent(String employeeCode) {
        return sellerRepository.existsByEmployeeCode(employeeCode);
    }

    @Override
    public Seller createSeller(SellerDetailsRequest request, User user) {
        if (checkIfPresent(request.getEmployeeCode())) {
            throw new SellerException("Seller already exists");
        }

        Seller seller = mapper.mapToSeller(request, user);

        return sellerRepository.save(seller);

    }

    @Override
    public Page<Seller> getSellerByOrderedByNumSales(Pageable pageable) {
        Pageable pageable1 = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by("salesCount").descending());

        return sellerRepository.findAll(pageable1);
    }

    @Override
    public Optional<Seller> findByUsername(String username) {
        return sellerRepository.findByUsername(username);
    }

    @Override
    public Optional<Seller> findById(UUID id) {
        return sellerRepository.findById(id);
    }
}
