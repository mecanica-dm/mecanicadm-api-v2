package com.mecanicadm.mecanicadm_api.core.workorders.service;

import com.mecanicadm.mecanicadm_api.core.shared.exception.DomainExceptionCore;
import com.mecanicadm.mecanicadm_api.core.workorders.adapter.repository.WorkOrderRepository;
import com.mecanicadm.mecanicadm_api.core.workorders.domain.WorkOrder;
import com.mecanicadm.mecanicadm_api.core.workorders.domain.WorkOrderBudget;
import com.mecanicadm.mecanicadm_api.core.workorders.domain.enums.WorkOrderBudgetStatus;
import com.mecanicadm.mecanicadm_api.core.workorders.exception.WorkOrderExceptions;
import com.mecanicadm.mecanicadm_api.core.workorders.usecase.command.SendWorkOrderBudgetCommand;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SendWorkOrderBudgetServiceTest {

    @Mock
    private WorkOrderRepository workOrderRepository;

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
                () -> assertEquals(HttpStatus.NOT_FOUND, exception.getStatus())
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
                () -> assertEquals(HttpStatus.NOT_FOUND, exception.getStatus())
        );
        verify(workOrderRepository, never()).save(any());
    }
}
