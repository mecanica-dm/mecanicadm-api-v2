package com.mecanicadm.mecanicadm_api.core.workorders.service;

import com.mecanicadm.mecanicadm_api.core.workorders.domain.WorkOrder;
import com.mecanicadm.mecanicadm_api.core.workorders.domain.WorkOrderBudget;
import com.mecanicadm.mecanicadm_api.core.workorders.domain.enums.WorkOrderBudgetStatus;
import com.mecanicadm.mecanicadm_api.core.workorders.exception.WorkOrderExceptions;
import com.mecanicadm.mecanicadm_api.core.workorders.usecase.command.SendWorkOrderBudgetCommand;
import com.mecanicadm.mecanicadm_api.infra.features.workorder.persistence.jpa.WorkOrderJpaRepository;
import com.mecanicadm.mecanicadm_api.shared.exception.DomainExceptionCore;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SendWorkOrderBudgetServiceTest {

    @Mock
    private WorkOrderJpaRepository workOrderRepository;

    @InjectMocks
    private SendWorkOrderBudgetService service;

    @Test
    @DisplayName("Deve enviar orçamento com sucesso")
    void shouldSendBudgetSuccessfully() {
        UUID workOrderId = UUID.randomUUID();
        WorkOrder workOrder = WorkOrder.create(UUID.randomUUID(), "ABC-1234", "Desc");
        WorkOrderBudget budget = WorkOrderBudget.create(workOrder, new BigDecimal("100.00"));
        workOrder.assignBudget(budget);

        when(workOrderRepository.findById(workOrderId)).thenReturn(Optional.of(workOrder));

        service.handle(new SendWorkOrderBudgetCommand(workOrderId));

        assertEquals(WorkOrderBudgetStatus.WAITING_DECISION, budget.getStatus());
        verify(workOrderRepository, times(1)).save(workOrder);
    }

    @Test
    @DisplayName("Deve lançar exceção quando ordem de serviço não for encontrada")
    void shouldThrowExceptionWhenWorkOrderNotFound() {
        UUID workOrderId = UUID.randomUUID();
        when(workOrderRepository.findById(workOrderId)).thenReturn(Optional.empty());

        SendWorkOrderBudgetCommand command = new SendWorkOrderBudgetCommand(workOrderId);

        DomainExceptionCore exception = assertThrows(
                DomainExceptionCore.class,
                () -> service.handle(command)
        );

        assertAll(
                () -> assertInstanceOf(WorkOrderExceptions.NotFound.class, exception),
                () -> assertEquals("work.order.not.found", exception.getMessageKey()),
                () -> assertEquals(404, exception.getStatus())
        );
        verify(workOrderRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve lançar exceção quando orçamento não for encontrado na ordem de serviço")
    void shouldThrowExceptionWhenBudgetNotFound() {
        UUID workOrderId = UUID.randomUUID();
        WorkOrder workOrder = WorkOrder.create(UUID.randomUUID(), "ABC-1234", "Desc");

        when(workOrderRepository.findById(workOrderId)).thenReturn(Optional.of(workOrder));

        SendWorkOrderBudgetCommand command = new SendWorkOrderBudgetCommand(workOrderId);

        DomainExceptionCore exception = assertThrows(
                DomainExceptionCore.class,
                () -> service.handle(command)
        );

        assertAll(
                () -> assertInstanceOf(WorkOrderExceptions.BudgetNotFound.class, exception),
                () -> assertEquals("work.order.budget.not.found", exception.getMessageKey()),
                () -> assertEquals(404, exception.getStatus())
        );
        verify(workOrderRepository, never()).save(any());
    }
}
