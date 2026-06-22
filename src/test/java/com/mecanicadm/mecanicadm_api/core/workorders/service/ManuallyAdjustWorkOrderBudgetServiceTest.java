package com.mecanicadm.mecanicadm_api.core.workorders.service;

import com.mecanicadm.mecanicadm_api.core.workorders.adapter.repository.WorkOrderRepository;
import com.mecanicadm.mecanicadm_api.core.workorders.domain.WorkOrder;
import com.mecanicadm.mecanicadm_api.core.workorders.domain.WorkOrderBudget;
import com.mecanicadm.mecanicadm_api.core.workorders.exception.WorkOrderExceptions;
import com.mecanicadm.mecanicadm_api.core.workorders.usecase.command.ManuallyAdjustWorkOrderBudgetCommand;
import com.mecanicadm.mecanicadm_api.shared.exception.DomainExceptionCore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ManuallyAdjustWorkOrderBudgetServiceTest {

    @Mock
    private WorkOrderRepository workOrderRepository;

    @InjectMocks
    private ManuallyAdjustWorkOrderBudgetService service;

    private WorkOrder workOrder;
    private UUID workOrderId;

    @BeforeEach
    void setUp() {
        workOrder = WorkOrder.create(UUID.randomUUID(), "ABC-1234", "Reparo");
        workOrderId = workOrder.getId();
    }

    @Test
    @DisplayName("Deve atualizar preco total do orcamento quando ja existir")
    void shouldUpdateTotalPriceWhenBudgetAlreadyExists() {
        BigDecimal oldPrice = new BigDecimal("100.00");
        BigDecimal newPrice = new BigDecimal("150.00");
        workOrder.assignBudget(WorkOrderBudget.create(workOrder, oldPrice));

        when(workOrderRepository.findById(workOrderId)).thenReturn(Optional.of(workOrder));

        service.handle(new ManuallyAdjustWorkOrderBudgetCommand(workOrderId, newPrice));

        assertAll(
                () -> assertTrue(workOrder.getBudget().isPresent()),
                () -> assertEquals(newPrice, workOrder.getBudget().get().getTotalPrice())
        );
        verify(workOrderRepository).save(workOrder);
    }

    @Test
    @DisplayName("Deve lancar excecao quando orcamento nao existir")
    void shouldThrowBudgetNotFoundWhenBudgetDoesNotExist() {
        BigDecimal newPrice = new BigDecimal("200.00");

        when(workOrderRepository.findById(workOrderId)).thenReturn(Optional.of(workOrder));

        ManuallyAdjustWorkOrderBudgetCommand command =
                new ManuallyAdjustWorkOrderBudgetCommand(workOrderId, newPrice);

        DomainExceptionCore exception = assertThrows(
                DomainExceptionCore.class,
                () -> service.handle(command)
        );

        assertAll(
                () -> assertInstanceOf(WorkOrderExceptions.BudgetNotFound.class, exception),
                () -> assertEquals("work.order.budget.not.found", exception.getMessageKey()),
                () -> assertEquals(404, exception.getStatus())
        );
    }

    @Test
    @DisplayName("Deve lancar excecao quando ordem de servico nao existir")
    void shouldThrowNotFoundWhenWorkOrderDoesNotExist() {
        when(workOrderRepository.findById(workOrderId)).thenReturn(Optional.empty());

        ManuallyAdjustWorkOrderBudgetCommand command =
                new ManuallyAdjustWorkOrderBudgetCommand(workOrderId, BigDecimal.TEN);

        DomainExceptionCore exception = assertThrows(
                DomainExceptionCore.class,
                () -> service.handle(command)
        );

        assertAll(
                () -> assertInstanceOf(WorkOrderExceptions.NotFound.class, exception),
                () -> assertEquals("work.order.not.found", exception.getMessageKey()),
                () -> assertEquals(404, exception.getStatus())
        );
    }
}
