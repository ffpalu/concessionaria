package com.ffpalu.concessionaria.middleware.interfaces;

import com.ffpalu.concessionaria.dto.request.VehicleRequest;
import com.ffpalu.concessionaria.dto.response.VehicleResponse;
import com.ffpalu.concessionaria.entity.Vehicle;
import com.ffpalu.concessionaria.middleware.VehicleMiddlewareImpl;
import com.ffpalu.concessionaria.service.interfaces.VehicleService;
import com.ffpalu.concessionaria.utils.Mapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.Year;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class VehicleMiddlewareTest {


    @Mock
    private VehicleService vehicleService;
    @Mock
    private Mapper mapper;
    @InjectMocks
    private VehicleMiddlewareImpl vehicleMiddleware;

    private final Vehicle vehicle = Vehicle.builder()
            .id(UUID.randomUUID()).plate("AB123CD").brand("Fiat").model("Panda")
            .used(false).year(Year.of(2023)).build();

    private final VehicleResponse response = VehicleResponse.builder()
            .id(vehicle.getId()).plate("AB123CD").brand("Fiat").model("Panda").build();

    @Test
    void createVehicleShouldDelegateAndMap() {
        VehicleRequest req = new VehicleRequest();
        when(vehicleService.createVehicle(req)).thenReturn(vehicle);
        when(mapper.mapToVehicleResponse(vehicle)).thenReturn(response);

        VehicleResponse result = vehicleMiddleware.createVehicle(req);

        assertEquals("AB123CD", result.getPlate());
    }

    @Test
    void updateVehicleShouldDelegateAndMap() {
        VehicleRequest req = new VehicleRequest();
        when(vehicleService.updateVehicle(req)).thenReturn(vehicle);
        when(mapper.mapToVehicleResponse(vehicle)).thenReturn(response);

        VehicleResponse result = vehicleMiddleware.updateVehicle(req);

        assertNotNull(result);
    }

    @Test
    void getAllVehicleShouldReturnMappedPage() {
        Pageable pageable = PageRequest.of(0, 10);
        when(vehicleService.getAllVehicle(pageable)).thenReturn(new PageImpl<>(List.of(vehicle)));
        when(mapper.mapToVehicleResponse(vehicle)).thenReturn(response);

        Page<VehicleResponse> result = vehicleMiddleware.getAllVehicle(pageable);

        assertEquals(1, result.getTotalElements());
    }

    @Test
    void getVehicleByIdShouldParseUUIDAndDelegate() {
        UUID id = vehicle.getId();
        when(vehicleService.getVehicleById(id)).thenReturn(vehicle);
        when(mapper.mapToVehicleResponse(vehicle)).thenReturn(response);

        VehicleResponse result = vehicleMiddleware.getVehicleById(id.toString());

        assertEquals("AB123CD", result.getPlate());
    }

    @Test
    void getVehicleByPlateShouldReturnMappedOptional() {
        when(vehicleService.getVehicleByPlate("AB123CD")).thenReturn(Optional.of(vehicle));
        when(mapper.mapToVehicleResponse(vehicle)).thenReturn(response);

        Optional<VehicleResponse> result = vehicleMiddleware.getVehicleByPlate("AB123CD");

        assertTrue(result.isPresent());
    }

    @Test
    void getVehicleByModelAndBrandShouldDelegate() {
        Pageable pageable = PageRequest.of(0, 10);
        when(vehicleService.getVehicleFromModelAndBrand("Panda", "Fiat", pageable))
                .thenReturn(new PageImpl<>(List.of(vehicle)));
        when(mapper.mapToVehicleResponse(vehicle)).thenReturn(response);

        Page<VehicleResponse> result = vehicleMiddleware.getVehicleByModelAndBrand("Panda", "Fiat", pageable);

        assertEquals(1, result.getTotalElements());
    }
}