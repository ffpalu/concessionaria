package com.ffpalu.concessionaria.service.interfaces;

import com.ffpalu.concessionaria.dto.request.VehicleRequest;
import com.ffpalu.concessionaria.dto.response.VehicleResponse;
import com.ffpalu.concessionaria.entity.Vehicle;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface VehicleService {

    Vehicle createVehicle(VehicleRequest vehicleRequest);

    Page<Vehicle> getAllVehicle(Pageable pageable);

    Optional<Vehicle> getVehicleByPlate(String plate);

    Vehicle getVehicleById(UUID id);

    Page<Vehicle> getVehicleFromModelAndBrand(String model, String brand, Pageable pageable);

    Vehicle updateVehicle(VehicleRequest vehicleRequest);

}
