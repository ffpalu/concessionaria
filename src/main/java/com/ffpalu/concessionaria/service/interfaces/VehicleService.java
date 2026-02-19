package com.ffpalu.concessionaria.service.interfaces;

import com.ffpalu.concessionaria.dto.request.RegistrationVehicleRequest;
import com.ffpalu.concessionaria.dto.response.VehicleResponse;
import com.ffpalu.concessionaria.entity.Vehicle;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface VehicleService {

    VehicleResponse createVehicle(RegistrationVehicleRequest vehicleRequest);

    Page<Vehicle> getAllVehicle(Pageable pageable);

    Page<VehicleResponse> getAllVehicleResponse(Pageable pageable);

    Optional<Vehicle> getVehicleByPlate(String plate);

    Optional<Vehicle> getVehicleById(UUID id);

    Optional<VehicleResponse> getVehicleResponseByPlate(String plate);

    Optional<VehicleResponse> getVehicleResponseById(UUID id);

    Page<Vehicle> getVehicleFromModelAndBrand(String model, String brand, Pageable pageable);

    Page<VehicleResponse> getVehicleResponseFromModelAndBrand(String model, String brand, Pageable pageable);

    Vehicle updateVehicle(RegistrationVehicleRequest vehicleRequest);

    VehicleResponse updateVehicleResponse(RegistrationVehicleRequest vehicleRequest);

}
