package com.ffpalu.concessionaria.service.interfaces;

import com.ffpalu.concessionaria.dto.request.VehicleRequest;
import com.ffpalu.concessionaria.entity.Vehicle;
import com.ffpalu.concessionaria.exceptions.VehicleException;
import com.ffpalu.concessionaria.repository.VehicleRepository;
import com.ffpalu.concessionaria.service.VehicleServiceImpl;
import com.ffpalu.concessionaria.utils.Mapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VehicleServiceTest {

    @Mock
    private VehicleRepository vehicleRepository;

    @Mock
    private Mapper mapper;

    @InjectMocks
    private VehicleServiceImpl vehicleService;


    @Test
    void createVehicleShouldSaveWhenPlateNotExists() {

        VehicleRequest request = new VehicleRequest();
        request.setPlate("AB123CD");

        Vehicle vehicle = Vehicle.builder().plate("AB123CD").build();
        Vehicle saved =  Vehicle.builder().id(UUID.randomUUID()).plate("AB123CD").build();

        when(vehicleRepository.existsByPlate("AB123CD")).thenReturn(false);
        when(mapper.mapToVehicle(request)).thenReturn(vehicle);
        when(vehicleRepository.save(vehicle)).thenReturn(saved);

        Vehicle result = vehicleService.createVehicle(request);

        assertEquals("AB123CD", result.getPlate());
        verify(vehicleRepository).save(any());


    }

    @Test
    void createVehicleShouldThrowWhenPlateExists() {
        VehicleRequest request = new VehicleRequest();
        request.setPlate("AB123CD");

        when(vehicleRepository.existsByPlate("AB123CD")).thenReturn(true);

        assertThrows(VehicleException.class,() ->  vehicleService.createVehicle(request));
        verify(vehicleRepository, never()).save(any());

    }

    @Test
    void getVehicleByPlateShouldReturnVehicleWhenPlateExists() {
        String plate = "AB123CD";

        Vehicle vehicle = Vehicle.builder().plate(plate).build();

        when(vehicleRepository.findByPlate(plate)).thenReturn(Optional.of(vehicle));

        Optional<Vehicle> vehicleTested = vehicleService.getVehicleByPlate(plate);
        assertTrue(vehicleTested.isPresent());
        assertEquals(vehicle, vehicleTested.get());
        assertEquals(plate, vehicleTested.get().getPlate());
    }

    @Test
    void getVehicleByPlateShouldReturnOptionalEmptyWhenPlateNotExists() {
        String plate = "AB123CD";

        when(vehicleRepository.findByPlate(plate)).thenReturn(Optional.empty());

        Optional<Vehicle> vehicleTested = vehicleService.getVehicleByPlate(plate);
        assertFalse(vehicleTested.isPresent());
    }

    @Test
    void updateVehicleShouldThrowWhenIdIsNull() {
        VehicleRequest request = new VehicleRequest();

        assertThrows(VehicleException.class,() ->  vehicleService.updateVehicle(request));
    }


    @Test
    void updateVehicleShouldThrowWhenIdIsNotFound() {
        VehicleRequest request = new VehicleRequest();
        UUID id = UUID.randomUUID();
        request.setId(id.toString());

        when(request.getId()).thenReturn(id.toString());
        when(vehicleRepository.findById(id)).thenReturn(Optional.empty());
        assertThrows(VehicleException.class,() ->  vehicleService.updateVehicle(request));
    }

    @Test
    void updateShouldReturnVehicleUpdatedWhenIdIsFound() {
        VehicleRequest request = new VehicleRequest();
        UUID id = UUID.randomUUID();
        request.setId(id.toString());
        request.setPlate("AB123CS");
        Vehicle vehicle = Vehicle.builder().id(id).plate("AB123CD").build();
        Vehicle vehicleUpdated =  Vehicle.builder().id(id).plate("AB123CS").build();


        when(vehicleRepository.findById(id)).thenReturn(Optional.of(vehicle));
        when(vehicleRepository.save(vehicleUpdated)).thenReturn(vehicleUpdated);

        Vehicle result = vehicleService.updateVehicle(request);

        assertEquals(vehicleUpdated, result);
        assertEquals(id, result.getId());
        assertEquals("AB123CS",  result.getPlate());

    }

}