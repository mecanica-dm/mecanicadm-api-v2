package com.mecanicadm.mecanicadm_api.core.vehicle.service;

import com.mecanicadm.mecanicadm_api.core.vehicle.adapter.repository.VehicleRepository;
import com.mecanicadm.mecanicadm_api.core.vehicle.domain.Vehicle;
import com.mecanicadm.mecanicadm_api.core.vehicle.exception.VehicleExceptions;
import com.mecanicadm.mecanicadm_api.core.vehicle.usecase.command.UpdateVehicleCommand;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateVehicleServiceTest {

    @Mock
    private VehicleRepository repository;

    @InjectMocks
    private UpdateVehicleService updateVehicleService;

    @Test
    @DisplayName("Deve atualizar o modelo do veículo com sucesso")
    void shouldUpdateVehicleSuccessfully() {
        String licensePlate = "ABC-1234";
        String newModel = "Novo Modelo";
        String newBrand = "Nova Marca";
        Short newYear = Short.valueOf("2030");
        UpdateVehicleCommand command = new UpdateVehicleCommand(licensePlate, newModel, newBrand, newYear);
        Vehicle vehicle = mock(Vehicle.class);

        when(repository.findById(licensePlate)).thenReturn(Optional.of(vehicle));
        when(repository.save(vehicle)).thenReturn(vehicle);
        when(vehicle.getLicensePlate()).thenReturn(licensePlate);

        String result = updateVehicleService.handle(command);

        assertEquals(licensePlate, result);
        verify(vehicle).update(newModel, newBrand, newYear);
        verify(repository).save(vehicle);
    }

    @Test
    @DisplayName("Deve lançar erro ao tentar atualizar veículo inexistente")
    void shouldThrowErrorUpdateVehicleNotFound() {
        String licensePlate = "XYZ-9999";
        UpdateVehicleCommand command = new UpdateVehicleCommand(licensePlate, "Polo", "VW", Short.valueOf("2025"));

        when(repository.findById(licensePlate)).thenReturn(Optional.empty());

        assertThrows(VehicleExceptions.NotFound.class, () -> updateVehicleService.handle(command));
        verify(repository, never()).save(any());
    }
}
