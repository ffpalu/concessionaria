package com.ffpalu.concessionaria.repository;

import com.ffpalu.concessionaria.entity.Vehicle;
import com.ffpalu.concessionaria.service.interfaces.VehicleService;
import com.ffpalu.concessionaria.utils.Mapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.test.context.ActiveProfiles;

import java.time.Year;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class VehicleRepositoryTest {

    @Autowired
    private  VehicleRepository vehicleRepository;

    @Autowired
    private TestEntityManager entityManager;



    @BeforeEach
    void setUp() {

        Vehicle mito = Vehicle.builder()
                .plate("ED570DB")
                .brand("Alfaromeo")
                .model("Mito")
                .used(true)
                .year(Year.of(2010))
                .numberOfKilometers(180000)
                .build();

        entityManager.persistAndFlush(mito);

    }

    @Test
    void findByPlateReturnsVehicleWhenPlateExists() {
        Optional<Vehicle> found =  vehicleRepository.findByPlate("ED570DB");

        assertTrue(found.isPresent());
        assertEquals("ED570DB", found.get().getPlate());
        assertEquals("Mito", found.get().getModel());
        assertEquals( Year.of(2010), found.get().getYear());
        assertEquals(180000, found.get().getNumberOfKilometers());
    }

    @Test
    void findByPlateReturnsOptionalEmptyWhenPlateNotExists() {
        Optional<Vehicle> found =  vehicleRepository.findByPlate("ED522FB");

        assertFalse(found.isPresent());
    }

    @Test
    void findByModelAndBrandShouldBeFound() {

        Page<Vehicle> founded = vehicleRepository.findByModelAndBrand("Mito", "Alfaromeo", null);

        assertTrue(founded.hasContent());
        assertEquals("Mito", founded.getContent().get(0).getModel());
        assertEquals("Alfaromeo", founded.getContent().get(0).getBrand());
        assertEquals(Year.of(2010), founded.getContent().get(0).getYear());
        assertEquals(180000, founded.getContent().get(0).getNumberOfKilometers());

    }
}