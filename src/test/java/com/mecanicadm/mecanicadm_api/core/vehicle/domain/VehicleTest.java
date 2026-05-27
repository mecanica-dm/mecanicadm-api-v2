package com.mecanicadm.mecanicadm_api.core.vehicle.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class VehicleTest {

    @Test
    @DisplayName("Deve instanciar um veículo corretamente")
    void shouldInstantiateVehicle() {
        String model = "City Hatch";
        String licensePlate = "ABC1234";
        String brand = "Honda";
        Short modelYear = 2022;

        Vehicle vehicle = new Vehicle(model, licensePlate, brand, modelYear);

        assertEquals(licensePlate, vehicle.getLicensePlate());
        assertEquals(model, vehicle.getModel());
        assertEquals(brand, vehicle.getBrand());
        assertEquals(modelYear, vehicle.getModelYear());
    }

    @Test
    @DisplayName("Deve permitir instanciar veículo com construtor vazio (exigência JPA)")
    void shouldInstantiateWithEmptyConstructor() {
        Vehicle vehicle = new Vehicle();
        assertNotNull(vehicle);
        assertNull(vehicle.getLicensePlate());
        assertNull(vehicle.getModel());
    }

    @Test
    @DisplayName("Deve atualizar o modelo do veículo com sucesso")
    void shouldUpdateVehicleModel() {
        Vehicle vehicle = new Vehicle("Fox", "ABC1234", "VW", Short.valueOf("2015"));
        String newModel = "Corolla";
        String newBrand = "Toyota";
        Short newYear = Short.valueOf("2015");

        vehicle.update(newModel, newBrand, newYear);

        assertEquals(newModel, vehicle.getModel());
    }
}
