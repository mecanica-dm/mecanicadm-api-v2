package com.mecanicadm.mecanicadm_api.core.vehicle.service;

import com.mecanicadm.mecanicadm_api.core.vehicle.adapter.repository.VehicleRepository;
import com.mecanicadm.mecanicadm_api.core.vehicle.domain.Vehicle;
import com.mecanicadm.mecanicadm_api.core.vehicle.exception.VehicleExceptions;
import com.mecanicadm.mecanicadm_api.core.vehicle.usecase.query.GetVehicleByIdQuery;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetVehicleByIdServiceTest {

    @Mock
    private VehicleRepository repository;

    @InjectMocks
    private GetVehicleByIdService service;

    @Test
    @DisplayName("Deve retornar um veículo quando a placa existir na base de dados")
    void whenVehicleExists_ShouldReturnVehicle() {
        String licensePlate = "BRA2E19";
        GetVehicleByIdQuery query = new GetVehicleByIdQuery(licensePlate);
        Vehicle expectedVehicle = mock(Vehicle.class);

        when(repository.findById(licensePlate)).thenReturn(Optional.of(expectedVehicle));

        Vehicle result = service.handle(query);

        assertNotNull(result);
        assertEquals(expectedVehicle, result);
        verify(repository, times(1)).findById(licensePlate);
    }

    @Test
    @DisplayName("Deve lançar NotFoundException quando o veículo não for encontrado")
    void whenVehicleDoesNotExist_ShouldThrowNotFoundException() {
        String licensePlate = "NOTFOUND";
        GetVehicleByIdQuery query = new GetVehicleByIdQuery(licensePlate);

        when(repository.findById(licensePlate)).thenReturn(Optional.empty());

        assertThrows(VehicleExceptions.NotFound.class, () -> service.handle(query));
        verify(repository, times(1)).findById(licensePlate);
    }
}