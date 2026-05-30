package com.mecanicadm.mecanicadm_api.core.vehicle.service;

import com.mecanicadm.mecanicadm_api.core.vehicle.domain.port.VehicleGateway;
import com.mecanicadm.mecanicadm_api.core.vehicle.exception.VehicleExceptions;
import com.mecanicadm.mecanicadm_api.core.vehicle.usecase.DeleteVehicleUseCase;
import com.mecanicadm.mecanicadm_api.core.vehicle.usecase.command.DeleteVehicleCommand;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeleteVehicleServiceTest {

    private VehicleGateway repository;

    private DeleteVehicleUseCase deleteVehicleUseCase;

    private DeleteVehicleCommand command;
    private String licensePlate;

    @BeforeEach
    void setUp() {
        repository = mock(VehicleGateway.class);
        deleteVehicleUseCase = new DeleteVehicleUseCase(repository);
        licensePlate = "ABC-1234";
        command = new DeleteVehicleCommand(licensePlate);
    }

    @Test
    @DisplayName("Should delete vehicle when it exists")
    void shouldDeleteVehicleWhenItExists() {
        when(repository.existsByLicensePlate(licensePlate)).thenReturn(true);

        deleteVehicleUseCase.execute(command);

        verify(repository, times(1)).existsByLicensePlate(licensePlate);
        verify(repository, times(1)).deleteByLicensePlate(licensePlate);
    }

    @Test
    @DisplayName("Should throw NotFound exception when vehicle does not exist")
    void shouldThrowNotFoundExceptionWhenVehicleDoesNotExist() {
        when(repository.existsByLicensePlate(licensePlate)).thenReturn(false);

        assertThrows(VehicleExceptions.NotFound.class, () -> deleteVehicleUseCase.execute(command));

        verify(repository, times(1)).existsByLicensePlate(licensePlate);
        verify(repository, never()).deleteByLicensePlate(anyString());
    }
}
