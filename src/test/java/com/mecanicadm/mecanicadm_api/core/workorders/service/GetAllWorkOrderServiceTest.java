package com.mecanicadm.mecanicadm_api.core.workorders.service;

import com.mecanicadm.mecanicadm_api.core.workorders.domain.WorkOrder;
import com.mecanicadm.mecanicadm_api.core.workorders.usecase.query.GetAllWorkOrdersQuery;
import com.mecanicadm.mecanicadm_api.infra.features.workorder.persistence.jpa.WorkOrderJpaRepository;
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
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetAllWorkOrderServiceTest {

    @Mock
    private WorkOrderJpaRepository repository;

    @InjectMocks
    private GetAllWorkOrderService service;

    @Test
    @DisplayName("Deve retornar uma página de ordens de serviço")
    void shouldReturnPageOfWorkOrders() {
        Pageable pageable = PageRequest.of(0, 10);
        UUID clientId = UUID.randomUUID();
        GetAllWorkOrdersQuery query = new GetAllWorkOrdersQuery(clientId, null, pageable);
        WorkOrder workOrder = WorkOrder.create(clientId, "ABC-1234", "Description");
        Page<WorkOrder> page = new PageImpl<>(List.of(workOrder), pageable, 1);

        when(repository.findAll(any(Specification.class), eq(pageable))).thenReturn(page);

        Page<WorkOrder> response = service.handle(query);

        assertNotNull(response);
        assertEquals(1, response.getTotalElements());
        assertEquals(clientId, response.getContent().get(0).getClientId());
        verify(repository).findAll(any(Specification.class), eq(pageable));
    }

    @Test
    @DisplayName("Deve retornar uma página vazia quando nenhuma ordem de serviço for encontrada")
    void shouldReturnEmptyPageWhenNoWorkOrdersFound() {
        Pageable pageable = PageRequest.of(0, 10);
        GetAllWorkOrdersQuery query = new GetAllWorkOrdersQuery(UUID.randomUUID(), null, pageable);
        Page<WorkOrder> page = new PageImpl<>(List.of(), pageable, 0);

        when(repository.findAll(any(Specification.class), eq(pageable))).thenReturn(page);

        Page<WorkOrder> response = service.handle(query);

        assertNotNull(response);
        assertTrue(response.isEmpty());
        verify(repository).findAll(any(Specification.class), eq(pageable));
    }

    @Test
    @DisplayName("Deve filtrar por clientId e retornar página vazia se não houver correspondência")
    void shouldFilterByClientId() {
        Pageable pageable = PageRequest.of(0, 10);
        UUID clientId = UUID.randomUUID();
        GetAllWorkOrdersQuery query = new GetAllWorkOrdersQuery(clientId, null, pageable);
        Page<WorkOrder> emptyPage = new PageImpl<>(List.of(), pageable, 0);

        when(repository.findAll(any(Specification.class), eq(pageable))).thenReturn(emptyPage);

        Page<WorkOrder> response = service.handle(query);

        assertNotNull(response);
        assertTrue(response.isEmpty());
        assertEquals(0, response.getTotalElements());
        verify(repository).findAll(any(Specification.class), eq(pageable));
    }

    @Test
    @DisplayName("Deve filtrar por placa")
    void shouldFilterByLicensePlate() {
        Pageable pageable = PageRequest.of(0, 10);
        String licensePlate = "ABC-1234";
        GetAllWorkOrdersQuery query = new GetAllWorkOrdersQuery(null, licensePlate, pageable);
        WorkOrder workOrder = WorkOrder.create(UUID.randomUUID(), licensePlate, "Description");
        Page<WorkOrder> page = new PageImpl<>(List.of(workOrder), pageable, 1);

        when(repository.findAll(any(Specification.class), eq(pageable))).thenReturn(page);

        Page<WorkOrder> response = service.handle(query);

        assertNotNull(response);
        assertEquals(1, response.getTotalElements());
        assertEquals(licensePlate, response.getContent().get(0).getVehicleId());
        verify(repository).findAll(any(Specification.class), eq(pageable));
    }

    @Test
    @DisplayName("Deve filtrar por clientId e placa")
    void shouldFilterByClientIdAndLicensePlate() {
        Pageable pageable = PageRequest.of(0, 10);
        UUID clientId = UUID.randomUUID();
        String licensePlate = "ABC-1234";
        GetAllWorkOrdersQuery query = new GetAllWorkOrdersQuery(clientId, licensePlate, pageable);
        WorkOrder workOrder = WorkOrder.create(clientId, licensePlate, "Description");
        Page<WorkOrder> page = new PageImpl<>(List.of(workOrder), pageable, 1);

        when(repository.findAll(any(Specification.class), eq(pageable))).thenReturn(page);

        Page<WorkOrder> response = service.handle(query);

        assertNotNull(response);
        assertEquals(1, response.getTotalElements());
        assertEquals(clientId, response.getContent().get(0).getClientId());
        assertEquals(licensePlate, response.getContent().get(0).getVehicleId());
        verify(repository).findAll(any(Specification.class), eq(pageable));
    }
}
