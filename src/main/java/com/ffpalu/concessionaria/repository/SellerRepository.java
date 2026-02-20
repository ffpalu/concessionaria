package com.ffpalu.concessionaria.repository;

import com.ffpalu.concessionaria.entity.Seller;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface SellerRepository extends JpaRepository<Seller, UUID> {
    boolean existsByEmployeeCode(String employeeCode);

    @Query("SELECT s FROM Seller s JOIN s.user u JOIN u.credential c WHERE c.username = :username ")
    Optional<Seller> findByUsername(String username);
}
