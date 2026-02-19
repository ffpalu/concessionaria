package com.ffpalu.concessionaria.repository;

import com.ffpalu.concessionaria.entity.Vehicle;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, UUID> {

    boolean existsByPlate(String plate);

    Page<Vehicle> findByModelAndBrand(String model, String brand, Pageable pageable);

}
