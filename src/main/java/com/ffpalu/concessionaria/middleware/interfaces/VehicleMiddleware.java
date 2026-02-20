package com.ffpalu.concessionaria.middleware.interfaces;

import com.ffpalu.concessionaria.dto.request.VehicleRequest;
import com.ffpalu.concessionaria.dto.response.VehicleResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface VehicleMiddleware {

    VehicleResponse createVehicle(VehicleRequest vehicle);

    VehicleResponse updateVehicle(VehicleRequest vehicle);

    Page<VehicleResponse> getAllVehicle(Pageable pageable);

    VehicleResponse getVehicleById(String id);

    Optional<VehicleResponse> getVehicleByPlate(String plate);

    Page<VehicleResponse> getVehicleByModelAndBrand(String model, String brand, Pageable pageable);


}
