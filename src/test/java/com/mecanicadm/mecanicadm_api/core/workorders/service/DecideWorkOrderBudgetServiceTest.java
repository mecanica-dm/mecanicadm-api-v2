package com.mecanicadm.mecanicadm_api.core.workorders.service;

import com.mecanicadm.mecanicadm_api.core.workorders.adapter.repository.WorkOrderRepository;
import com.mecanicadm.mecanicadm_api.core.workorders.domain.WorkOrder;
import com.mecanicadm.mecanicadm_api.core.workorders.domain.WorkOrderBudget;
import com.mecanicadm.mecanicadm_api.core.workorders.domain.enums.WorkOrderBudgetStatus;
import com.mecanicadm.mecanicadm_api.core.workorders.domain.enums.WorkOrderStatus;
import com.mecanicadm.mecanicadm_api.core.workorders.exception.WorkOrderExceptions;
import com.mecanicadm.mecanicadm_api.core.workorders.usecase.command.DecideWorkOrderBudgetCommand;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DecideWorkOrderBudgetServiceTest {

    @Mock
    private WorkOrderRepository workOrderRepository;

    @InjectMocks
    private DecideWorkOrderBudgetService service;

    @Test
    @DisplayName("Deve aprovar orçamento com sucesso")
    void shouldApproveBudgetSuccessfully() {
        UUID workOrderId = UUID.randomUUID();
        WorkOrder workOrder = WorkOrder.create(UUID.randomUUID(), "ABC-1234", "Desc");
        WorkOrderBudget budget = WorkOrderBudget.create(workOrder, new BigDecimal("100.00"));
        budget.send();
        workOrder.assignBudget(budget);

        DecideWorkOrderBudgetCommand cmd = new DecideWorkOrderBudgetCommand(workOrderId, WorkOrderBudgetStatus.APPROVED, null);

        when(workOrderRepository.findById(workOrderId)).thenReturn(Optional.of(workOrder));

        service.handle(cmd);

        assertEquals(WorkOrderBudgetStatus.APPROVED, budget.getStatus());
        assertEquals(WorkOrderStatus.AWAITING_EXECUTION, workOrder.getStatus());
        verify(workOrderRepository, times(1)).save(workOrder);
    }

    @Test
    @DisplayName("Deve solicitar alterações no orçamento com sucesso")
    void shouldRequestChangesSuccessfully() {
        UUID workOrderId = UUID.randomUUID();
        WorkOrder workOrder = WorkOrder.create(UUID.randomUUID(), "ABC-1234", "Desc");
        WorkOrderBudget budget = WorkOrderBudget.create(workOrder, new BigDecimal("100.00"));
        budget.send();
        workOrder.assignBudget(budget);

        DecideWorkOrderBudgetCommand cmd = new DecideWorkOrderBudgetCommand(workOrderId, WorkOrderBudgetStatus.CHANGES_REQUESTED, "Muito caro");

        when(workOrderRepository.findById(workOrderId)).thenReturn(Optional.of(workOrder));

        service.handle(cmd);

        assertEquals(WorkOrderBudgetStatus.CHANGES_REQUESTED, budget.getStatus());
        assertEquals(WorkOrderStatus.CHANGES_REQUESTED, workOrder.getStatus());
        assertEquals("Muito caro", budget.getRejectionReason());
        verify(workOrderRepository, times(1)).save(workOrder);
    }

    @Test
    @DisplayName("Deve rejeitar orçamento (cancelar ordem de serviço) com sucesso")
    void shouldRejectBudgetSuccessfully() {
        UUID workOrderId = UUID.randomUUID();
        WorkOrder workOrder = WorkOrder.create(UUID.randomUUID(), "ABC-1234", "Desc");
        WorkOrderBudget budget = WorkOrderBudget.create(workOrder, new BigDecimal("100.00"));
        budget.send();
        workOrder.assignBudget(budget);

        DecideWorkOrderBudgetCommand cmd = new DecideWorkOrderBudgetCommand(workOrderId, WorkOrderBudgetStatus.REJECTED, "Não vou fazer");

        when(workOrderRepository.findById(workOrderId)).thenReturn(Optional.of(workOrder));

        service.handle(cmd);

        assertEquals(WorkOrderBudgetStatus.REJECTED, budget.getStatus());
        assertEquals(WorkOrderStatus.CANCELLED, workOrder.getStatus());
        verify(workOrderRepository, times(1)).save(workOrder);
    }

    @Test
    @DisplayName("Deve lançar exceção ao decidir orçamento que não está aguardando decisão")
    void shouldThrowExceptionWhenBudgetIsNotWaitingDecision() {
        UUID workOrderId = UUID.randomUUID();
        WorkOrder workOrder = WorkOrder.create(UUID.randomUUID(), "ABC-1234", "Desc");
        WorkOrderBudget budget = WorkOrderBudget.create(workOrder, new BigDecimal("100.00"));
        workOrder.assignBudget(budget);

        DecideWorkOrderBudgetCommand cmd = new DecideWorkOrderBudgetCommand(workOrderId, WorkOrderBudgetStatus.APPROVED, null);

        when(workOrderRepository.findById(workOrderId)).thenReturn(Optional.of(workOrder));

        assertThrows(WorkOrderExceptions.BudgetNotWaitingDecision.class, () -> service.handle(cmd));
        verify(workOrderRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve lançar exceção ao decidir sem motivo em rejeição")
    void shouldThrowExceptionWhenReasonIsMissingOnRejected() {
        UUID workOrderId = UUID.randomUUID();
        WorkOrder workOrder = WorkOrder.create(UUID.randomUUID(), "ABC-1234", "Desc");
        WorkOrderBudget budget = WorkOrderBudget.create(workOrder, new BigDecimal("100.00"));
        budget.send();
        workOrder.assignBudget(budget);

        DecideWorkOrderBudgetCommand cmd = new DecideWorkOrderBudgetCommand(workOrderId, WorkOrderBudgetStatus.REJECTED, null);

        when(workOrderRepository.findById(workOrderId)).thenReturn(Optional.of(workOrder));

        assertThrows(WorkOrderExceptions.BudgetRejectionReasonRequired.class, () -> service.handle(cmd));
        verify(workOrderRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve lançar exceção ao decidir sem motivo em solicitação de alteração")
    void shouldThrowExceptionWhenReasonIsMissingOnChangesRequested() {
        UUID workOrderId = UUID.randomUUID();
        WorkOrder workOrder = WorkOrder.create(UUID.randomUUID(), "ABC-1234", "Desc");
        WorkOrderBudget budget = WorkOrderBudget.create(workOrder, new BigDecimal("100.00"));
        budget.send();
        workOrder.assignBudget(budget);

        DecideWorkOrderBudgetCommand cmd = new DecideWorkOrderBudgetCommand(workOrderId, WorkOrderBudgetStatus.CHANGES_REQUESTED, "  ");

        when(workOrderRepository.findById(workOrderId)).thenReturn(Optional.of(workOrder));

        assertThrows(WorkOrderExceptions.BudgetRejectionReasonRequired.class, () -> service.handle(cmd));
        verify(workOrderRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve lançar exceção quando ordem de serviço não for encontrada")
    void shouldThrowExceptionWhenWorkOrderNotFound() {
        UUID workOrderId = UUID.randomUUID();
        DecideWorkOrderBudgetCommand cmd = new DecideWorkOrderBudgetCommand(workOrderId, WorkOrderBudgetStatus.APPROVED, null);

        when(workOrderRepository.findById(workOrderId)).thenReturn(Optional.empty());

        assertThrows(WorkOrderExceptions.NotFound.class, () -> service.handle(cmd));
        verify(workOrderRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve lançar exceção quando orçamento não existir na ordem de serviço")
    void shouldThrowExceptionWhenBudgetNotFound() {
        UUID workOrderId = UUID.randomUUID();
        WorkOrder workOrder = WorkOrder.create(UUID.randomUUID(), "ABC-1234", "Desc");
        DecideWorkOrderBudgetCommand cmd = new DecideWorkOrderBudgetCommand(workOrderId, WorkOrderBudgetStatus.APPROVED, null);

        when(workOrderRepository.findById(workOrderId)).thenReturn(Optional.of(workOrder));

        assertThrows(WorkOrderExceptions.BudgetNotFound.class, () -> service.handle(cmd));
        verify(workOrderRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve lançar exceção para decisão inválida")
    void shouldThrowExceptionForInvalidDecision() {
        UUID workOrderId = UUID.randomUUID();
        WorkOrder workOrder = WorkOrder.create(UUID.randomUUID(), "ABC-1234", "Desc");
        WorkOrderBudget budget = WorkOrderBudget.create(workOrder, new BigDecimal("100.00"));
        budget.send();
        workOrder.assignBudget(budget);

        DecideWorkOrderBudgetCommand cmd = new DecideWorkOrderBudgetCommand(workOrderId, WorkOrderBudgetStatus.WAITING_DECISION, null);

        when(workOrderRepository.findById(workOrderId)).thenReturn(Optional.of(workOrder));

        assertThrows(WorkOrderExceptions.BudgetDecisionInvalid.class, () -> service.handle(cmd));
        verify(workOrderRepository, never()).save(any());
    }
}
