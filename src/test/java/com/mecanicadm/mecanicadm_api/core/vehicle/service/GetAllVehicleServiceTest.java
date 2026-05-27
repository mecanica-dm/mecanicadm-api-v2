package com.mecanicadm.mecanicadm_api.core.vehicle.service;

import com.mecanicadm.mecanicadm_api.core.vehicle.adapter.repository.VehicleRepository;
import com.mecanicadm.mecanicadm_api.core.vehicle.domain.Vehicle;
import com.mecanicadm.mecanicadm_api.core.vehicle.usecase.query.GetAllVehiclesQuery;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetAllVehicleServiceTest {

    @Mock
    private VehicleRepository repository;

    @InjectMocks
    private GetAllVehicleService service;

    @Test
    @DisplayName("Deve retornar página de veículos sem filtros quando a query estiver vazia")
    void shouldReturnVehiclesPageWhenNoFiltersProvided() {
        Pageable pageable = PageRequest.of(0, 10);
        GetAllVehiclesQuery query = new GetAllVehiclesQuery(null, null, null, null, pageable);
        Page<Vehicle> expectedPage = new PageImpl<>(List.of(mock(Vehicle.class)));

        when(repository.findAll(any(Specification.class), eq(pageable))).thenReturn(expectedPage);

        Page<Vehicle> result = service.handle(query);

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        verify(repository).findAll(any(Specification.class), eq(pageable));
    }

    @Test
    @DisplayName("Deve aplicar filtro de placa quando fornecido na query")
    void shouldApplyLicensePlateFilter() {
        Pageable pageable = PageRequest.of(0, 10);
        GetAllVehiclesQuery query = new GetAllVehiclesQuery("ABC-1234", null, null, null, pageable);
        Page<Vehicle> expectedPage = new PageImpl<>(List.of(mock(Vehicle.class)));

        when(repository.findAll(any(Specification.class), eq(pageable))).thenReturn(expectedPage);

        Page<Vehicle> result = service.handle(query);

        assertNotNull(result);
        verify(repository).findAll(any(Specification.class), eq(pageable));
    }

    @Test
    @DisplayName("Deve aplicar filtro de modelo quando fornecido na query")
    void shouldApplyModelFilter() {
        Pageable pageable = PageRequest.of(0, 10);
        GetAllVehiclesQuery query = new GetAllVehiclesQuery(null, "Civic", null, null, pageable);
        Page<Vehicle> expectedPage = new PageImpl<>(List.of(mock(Vehicle.class)));

        when(repository.findAll(any(Specification.class), eq(pageable))).thenReturn(expectedPage);

        Page<Vehicle> result = service.handle(query);

        assertNotNull(result);
        verify(repository).findAll(any(Specification.class), eq(pageable));
    }
}