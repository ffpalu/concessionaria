package com.ffpalu.concessionaria.repository;

import com.ffpalu.concessionaria.entity.Seller;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SellerRepository extends JpaRepository<Seller, UUID> {
    boolean existsByEmployeeCode(String employeeCode);
}
