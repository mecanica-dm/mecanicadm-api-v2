package com.mecanicadm.mecanicadm_api.core.vehicle.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class VehicleExceptionsTest {

    @Test
    @DisplayName("Deve cobrir o construtor privado de VehicleExceptions")
    void shouldCoverPrivateConstructor() throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Constructor<VehicleExceptions> constructor = VehicleExceptions.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        VehicleExceptions instance = constructor.newInstance();
        assertNotNull(instance);
    }

    @Test
    @DisplayName("Deve instanciar todas as exceções de Vehicle para garantir cobertura")
    void shouldInstantiateAllExceptions() {
        var notFound = new VehicleExceptions.NotFound();
        assertEquals(HttpStatus.NOT_FOUND, notFound.getStatus());
        assertEquals("vehicle.not.found", notFound.getMessageKey());

        var vehicleExists = new VehicleExceptions.VehicleExists();
        assertEquals(HttpStatus.BAD_REQUEST, vehicleExists.getStatus());
        assertEquals("vehicle.licensePlate.exists", vehicleExists.getMessageKey());
    }
}
