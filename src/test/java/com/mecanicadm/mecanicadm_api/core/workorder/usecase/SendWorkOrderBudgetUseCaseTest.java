package com.mecanicadm.mecanicadm_api.core.workorder.usecase;

import com.mecanicadm.mecanicadm_api.core.workorder.domain.WorkOrder;
import com.mecanicadm.mecanicadm_api.core.workorder.domain.WorkOrderBudget;
import com.mecanicadm.mecanicadm_api.core.workorder.domain.port.WorkOrderGateway;
import com.mecanicadm.mecanicadm_api.core.workorder.exception.WorkOrderExceptions;
import com.mecanicadm.mecanicadm_api.core.workorder.usecase.command.SendWorkOrderBudgetCommand;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SendWorkOrderBudgetUseCaseTest {

    @Mock
    private WorkOrderGateway gateway;

    @InjectMocks
    private SendWorkOrderBudgetUseCase useCase;

    @Test
    @DisplayName("Deve enviar o orçamento com sucesso")
    void shouldSendBudgetSuccessfully() {
        UUID workOrderId = UUID.randomUUID();
        WorkOrder workOrder = mock(WorkOrder.class);
        WorkOrderBudget budget = mock(WorkOrderBudget.class);
        when(gateway.findById(workOrderId)).thenReturn(Optional.of(workOrder));
        when(workOrder.getBudget()).thenReturn(Optional.of(budget));
        when(workOrder.getId()).thenReturn(workOrderId);
        when(workOrder.getClientId()).thenReturn(UUID.randomUUID());
        when(workOrder.getVehicleId()).thenReturn("ABC-1234");
        when(budget.getTotalPrice()).thenReturn(java.math.BigDecimal.valueOf(500));
        when(budget.getStatus()).thenReturn(
                com.mecanicadm.mecanicadm_api.core.workorder.domain.enums.WorkOrderBudgetStatus.WAITING_DECISION);

        useCase.execute(new SendWorkOrderBudgetCommand(workOrderId));

        verify(budget).send();
        verify(gateway).update(workOrder);
    }

    @Test
    @DisplayName("Deve lançar exceção quando o orçamento não for encontrado")
    void shouldThrowExceptionWhenBudgetNotFound() {
        UUID workOrderId = UUID.randomUUID();
        WorkOrder workOrder = mock(WorkOrder.class);
        when(gateway.findById(workOrderId)).thenReturn(Optional.of(workOrder));
        when(workOrder.getBudget()).thenReturn(Optional.empty());

        assertThrows(WorkOrderExceptions.BudgetNotFound.class,
                () -> useCase.execute(new SendWorkOrderBudgetCommand(workOrderId)));

        verify(gateway, never()).update(any());
    }
}
