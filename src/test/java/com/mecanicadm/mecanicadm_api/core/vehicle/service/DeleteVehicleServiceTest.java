package com.mecanicadm.mecanicadm_api.core.vehicle.service;

import com.mecanicadm.mecanicadm_api.core.vehicle.adapter.repository.VehicleRepository;
import com.mecanicadm.mecanicadm_api.core.vehicle.exception.VehicleExceptions;
import com.mecanicadm.mecanicadm_api.core.vehicle.usecase.command.DeleteVehicleCommand;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DeleteVehicleServiceTest {

    @Mock
    private VehicleRepository repository;

    @InjectMocks
    private DeleteVehicleService deleteVehicleService;

    private DeleteVehicleCommand command;
    private String licensePlate;

    @BeforeEach
    void setUp() {
        licensePlate = "ABC-1234";
        command = new DeleteVehicleCommand(licensePlate);
    }

    @Test
    @DisplayName("Should delete vehicle when it exists")
    void shouldDeleteVehicleWhenItExists() {
        when(repository.existsById(licensePlate)).thenReturn(true);

        deleteVehicleService.handle(command);

        verify(repository, times(1)).existsById(licensePlate);
        verify(repository, times(1)).deleteById(licensePlate);
    }

    @Test
    @DisplayName("Should throw NotFound exception when vehicle does not exist")
    void shouldThrowNotFoundExceptionWhenVehicleDoesNotExist() {
        when(repository.existsById(licensePlate)).thenReturn(false);

        assertThrows(VehicleExceptions.NotFound.class, () -> deleteVehicleService.handle(command));

        verify(repository, times(1)).existsById(licensePlate);
        verify(repository, never()).deleteById(anyString());
    }
}
