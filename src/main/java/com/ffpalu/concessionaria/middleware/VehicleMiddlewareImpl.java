package com.ffpalu.concessionaria.middleware;

import com.ffpalu.concessionaria.dto.request.VehicleRequest;
import com.ffpalu.concessionaria.dto.response.VehicleResponse;
import com.ffpalu.concessionaria.entity.Vehicle;
import com.ffpalu.concessionaria.middleware.interfaces.VehicleMiddleware;
import com.ffpalu.concessionaria.service.interfaces.VehicleService;
import com.ffpalu.concessionaria.utils.Mapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class VehicleMiddlewareImpl implements VehicleMiddleware {

    private final VehicleService vehicleService;
    private final Mapper mapper;

    @Override
    public VehicleResponse createVehicle(VehicleRequest vehicle) {
        Vehicle vehicleCreated = vehicleService.createVehicle(vehicle);

        return mapper.mapToVehicleResponse(vehicleCreated);
    }

    @Override
    public VehicleResponse updateVehicle(VehicleRequest vehicle) {
        Vehicle vehicleUpdated = vehicleService.updateVehicle(vehicle);
        return mapper.mapToVehicleResponse(vehicleUpdated);
    }

    @Override
    public Page<VehicleResponse> getAllVehicle(Pageable pageable) {
        Page<Vehicle> vehicles = vehicleService.getAllVehicle(pageable);
        return vehicles.map(mapper::mapToVehicleResponse);
    }

    @Override
    public VehicleResponse getVehicleById(String id) {
        Vehicle vehicleObtained = vehicleService.getVehicleById(UUID.fromString(id));
        return mapper.mapToVehicleResponse(vehicleObtained);
    }

    @Override
    public Optional<VehicleResponse> getVehicleByPlate(String plate) {
        Optional<Vehicle> vehicle = vehicleService.getVehicleByPlate(plate);
        return vehicle.map(mapper::mapToVehicleResponse);
    }

    @Override
    public Page<VehicleResponse> getVehicleByModelAndBrand(String model, String brand, Pageable pageable) {
        Page<Vehicle> vehicles = vehicleService.getVehicleFromModelAndBrand(model, brand, pageable);
        return vehicles.map(mapper::mapToVehicleResponse);
    }
}
