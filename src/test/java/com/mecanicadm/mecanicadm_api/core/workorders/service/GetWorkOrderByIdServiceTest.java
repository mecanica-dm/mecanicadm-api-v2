package com.mecanicadm.mecanicadm_api.core.workorders.service;

import com.mecanicadm.mecanicadm_api.core.workorders.domain.WorkOrder;
import com.mecanicadm.mecanicadm_api.core.workorders.exception.WorkOrderExceptions;
import com.mecanicadm.mecanicadm_api.core.workorders.usecase.query.GetWorkOrderByIdQuery;
import com.mecanicadm.mecanicadm_api.infra.features.workorder.persistence.jpa.WorkOrderJpaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetWorkOrderByIdServiceTest {

    @Mock
    private WorkOrderJpaRepository repository;

    @InjectMocks
    private GetWorkOrderByIdService service;

    @Test
    @DisplayName("Deve retornar uma ordem de serviço quando o ID existir")
    void shouldReturnWorkOrderWhenIdExists() {
        UUID id = UUID.randomUUID();
        GetWorkOrderByIdQuery query = new GetWorkOrderByIdQuery(id);
        WorkOrder workOrder = WorkOrder.create(UUID.randomUUID(), "ABC-1234", "Description");

        when(repository.findById(id)).thenReturn(Optional.of(workOrder));

        WorkOrder response = service.handle(query);

        assertNotNull(response);
        assertEquals(workOrder, response);
        verify(repository).findById(id);
    }

    @Test
    @DisplayName("Deve lançar exceção quando a ordem de serviço não for encontrada")
    void shouldThrowExceptionWhenWorkOrderNotFound() {
        UUID id = UUID.randomUUID();
        GetWorkOrderByIdQuery query = new GetWorkOrderByIdQuery(id);
        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThrows(WorkOrderExceptions.NotFound.class, () -> service.handle(query));
        verify(repository).findById(id);
    }
}
