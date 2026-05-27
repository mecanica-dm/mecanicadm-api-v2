package com.mecanicadm.mecanicadm_api.core.workorders.service;

import com.mecanicadm.mecanicadm_api.core.workorders.adapter.repository.WorkOrderRepository;
import com.mecanicadm.mecanicadm_api.core.workorders.domain.WorkOrder;
import com.mecanicadm.mecanicadm_api.core.workorders.exception.WorkOrderExceptions;
import com.mecanicadm.mecanicadm_api.core.workorders.usecase.command.UpdateWorkOrderCommand;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UpdateWorkOrderServiceTest {

    @Mock
    private WorkOrderRepository repository;

    @InjectMocks
    private UpdateWorkOrderService updateWorkOrderService;

    private UUID workOrderId;
    private UpdateWorkOrderCommand command;

    @BeforeEach
    void setUp() {
        workOrderId = UUID.randomUUID();
        command = new UpdateWorkOrderCommand(
                workOrderId,
                "NEW-123",
                UUID.randomUUID(),
                "Descrição atualizada"
        );
    }

    @Test
    @DisplayName("Deve atualizar uma ordem de serviço com sucesso")
    void shouldUpdateWorkOrderSuccessfully() {
        WorkOrder workOrder = WorkOrder.create(UUID.randomUUID(), "OLD-123", "Descrição antiga");
        when(repository.findById(workOrderId)).thenReturn(Optional.of(workOrder));

        updateWorkOrderService.handle(command);

        verify(repository).findById(workOrderId);
        verify(repository).save(workOrder);
        assertEquals(command.clientId(), workOrder.getClientId());
        assertEquals(command.vehicleId(), workOrder.getVehicleId());
        assertEquals(command.description(), workOrder.getDescription());
    }

    @Test
    @DisplayName("Deve lancar excecao quando ordem de servico nao for encontrada")
    void shouldThrowExceptionWhenWorkOrderNotFound() {
        when(repository.findById(workOrderId)).thenReturn(Optional.empty());

        assertThrows(WorkOrderExceptions.NotFound.class, () -> updateWorkOrderService.handle(command));

        verify(repository).findById(workOrderId);
    }
}
