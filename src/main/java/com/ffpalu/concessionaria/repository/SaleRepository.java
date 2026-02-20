package com.ffpalu.concessionaria.repository;

import com.ffpalu.concessionaria.entity.Sale;
import com.ffpalu.concessionaria.entity.Seller;
import com.ffpalu.concessionaria.entity.Vehicle;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface SaleRepository extends JpaRepository<Sale, UUID> {
    boolean existsByVehicleId(UUID vehicle);

    Page<Sale> findBySellerId(UUID seller, Pageable pageable);
}
