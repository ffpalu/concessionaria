package com.ffpalu.concessionaria.controller;

import com.ffpalu.concessionaria.controller.interfaces.VehicleController;
import com.ffpalu.concessionaria.dto.request.RegistrationVehicleRequest;
import com.ffpalu.concessionaria.dto.response.VehicleResponse;
import com.ffpalu.concessionaria.exceptions.VehicleException;
import com.ffpalu.concessionaria.service.interfaces.VehicleService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class VehicleControllerImpl implements VehicleController {

    private final VehicleService vehicleService;

    @Override
    public ResponseEntity<VehicleResponse> createVehicle(RegistrationVehicleRequest vehicle) {
        VehicleResponse response = vehicleService.createVehicle(vehicle);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<VehicleResponse> updateVehicle(RegistrationVehicleRequest vehicle) {
        VehicleResponse response = vehicleService.updateVehicleResponse(vehicle);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<Page<VehicleResponse>> getAllVehicle(Pageable pageable) {
        Page<VehicleResponse> allVehicle = vehicleService.getAllVehicleResponse(pageable);
        return ResponseEntity.ok(allVehicle);
    }

    @Override
    public ResponseEntity<VehicleResponse> getVehicleById(String id) {
        VehicleResponse vehicleResponse = vehicleService.getVehicleResponseById(UUID.fromString(id))
                .orElseThrow(() -> new VehicleException("vehicle not found"));

        return ResponseEntity.ok(vehicleResponse);
    }

    @Override
    public ResponseEntity<Optional<VehicleResponse>> getVehicleByPlate(String plate) {
        Optional<VehicleResponse> response = vehicleService.getVehicleResponseByPlate(plate);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<Page<VehicleResponse>> getVehicleByModelAndBrand(String model, String brand, Pageable pageable) {
        Page<VehicleResponse> response = vehicleService.getVehicleResponseFromModelAndBrand(model,brand, pageable);
        return ResponseEntity.ok(response);
    }
}
