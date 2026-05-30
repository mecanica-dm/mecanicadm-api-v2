package com.mecanicadm.mecanicadm_api.core.workorders.service;

import com.mecanicadm.mecanicadm_api.core.shared.exception.DomainExceptionCore;
import com.mecanicadm.mecanicadm_api.core.workorders.adapter.repository.WorkOrderRepository;
import com.mecanicadm.mecanicadm_api.core.workorders.domain.WorkOrder;
import com.mecanicadm.mecanicadm_api.core.workorders.domain.WorkOrderBudget;
import com.mecanicadm.mecanicadm_api.core.workorders.domain.enums.WorkOrderBudgetStatus;
import com.mecanicadm.mecanicadm_api.core.workorders.exception.WorkOrderExceptions;
import com.mecanicadm.mecanicadm_api.core.workorders.usecase.command.CalculateWorkOrderBudgetCommand;
import org.junit.jupiter.api.BeforeEach;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CalculateWorkOrderBudgetServiceTest {

    @Mock
    private WorkOrderRepository workOrderRepository;

    @InjectMocks
    private CalculateWorkOrderBudgetService service;

    private UUID workOrderId;
    private WorkOrder workOrder;

    @BeforeEach
    void setUp() {
        workOrderId = UUID.randomUUID();
        workOrder = WorkOrder.create(UUID.randomUUID(), "ABC-1234", "Reparo geral");
    }

    @Test
    @DisplayName("Deve calcular um novo orçamento para uma ordem de serviço sem orçamento anterior")
    void shouldCalculateNewBudgetWhenNoneExists() {
        CalculateWorkOrderBudgetCommand cmd = new CalculateWorkOrderBudgetCommand(workOrderId);
        
        when(workOrderRepository.findById(workOrderId)).thenReturn(Optional.of(workOrder));
        when(workOrderRepository.sumMaterialsTotalByWorkOrderId(workOrderId)).thenReturn(new BigDecimal("150.00"));
        when(workOrderRepository.sumLaborTotalByWorkOrderId(workOrderId)).thenReturn(new BigDecimal("200.00"));
        when(workOrderRepository.save(any(WorkOrder.class))).thenReturn(workOrder);

        UUID resultId = service.handle(cmd);
        WorkOrderBudget budget = workOrder.getBudget().orElseThrow();

        assertAll(
                () -> assertNotNull(resultId),
                () -> assertEquals(new BigDecimal("350.00"), budget.getTotalPrice()),
                () -> assertEquals(WorkOrderBudgetStatus.PENDING, budget.getStatus())
        );
        verify(workOrderRepository, times(1)).save(workOrder);
    }

    @Test
    @DisplayName("Deve recalcular o orçamento existente quando já houver um atribuído")
    void shouldRecalculateExistingBudget() {
        CalculateWorkOrderBudgetCommand cmd = new CalculateWorkOrderBudgetCommand(workOrderId);
        
        when(workOrderRepository.findById(workOrderId)).thenReturn(Optional.of(workOrder));
        when(workOrderRepository.sumMaterialsTotalByWorkOrderId(workOrderId)).thenReturn(new BigDecimal("50.00"));
        when(workOrderRepository.sumLaborTotalByWorkOrderId(workOrderId)).thenReturn(new BigDecimal("50.00"));
        when(workOrderRepository.save(any(WorkOrder.class))).thenReturn(workOrder);

        service.handle(cmd); 
        
        when(workOrderRepository.sumMaterialsTotalByWorkOrderId(workOrderId)).thenReturn(new BigDecimal("100.00"));
        when(workOrderRepository.sumLaborTotalByWorkOrderId(workOrderId)).thenReturn(new BigDecimal("100.00"));

        service.handle(cmd);

        WorkOrderBudget budget = workOrder.getBudget().orElseThrow();
        assertAll(
                () -> assertEquals(new BigDecimal("200.00"), budget.getTotalPrice()),
                () -> assertEquals(WorkOrderBudgetStatus.PENDING, budget.getStatus())
        );
        verify(workOrderRepository, times(2)).save(workOrder);
    }

    @Test
    @DisplayName("Deve tratar totais nulos do repositório como zero")
    void shouldTreatNullTotalsAsZero() {
        CalculateWorkOrderBudgetCommand cmd = new CalculateWorkOrderBudgetCommand(workOrderId);

        when(workOrderRepository.findById(workOrderId)).thenReturn(Optional.of(workOrder));
        when(workOrderRepository.sumMaterialsTotalByWorkOrderId(workOrderId)).thenReturn(null);
        when(workOrderRepository.sumLaborTotalByWorkOrderId(workOrderId)).thenReturn(null);
        when(workOrderRepository.save(any(WorkOrder.class))).thenReturn(workOrder);

        service.handle(cmd);

        WorkOrderBudget budget = workOrder.getBudget().orElseThrow();
        assertEquals(BigDecimal.ZERO, budget.getTotalPrice());
        verify(workOrderRepository, times(1)).save(workOrder);
    }

    @Test
    @DisplayName("Deve lançar exceção quando a ordem de serviço não for encontrada")
    void shouldThrowExceptionWhenWorkOrderNotFound() {
        CalculateWorkOrderBudgetCommand cmd = new CalculateWorkOrderBudgetCommand(workOrderId);
        when(workOrderRepository.findById(workOrderId)).thenReturn(Optional.empty());

        DomainExceptionCore exception = assertThrows(
                DomainExceptionCore.class,
                () -> service.handle(cmd)
        );

        assertAll(
                () -> assertInstanceOf(WorkOrderExceptions.NotFound.class, exception),
                () -> assertEquals("work.order.not.found", exception.getMessageKey()),
                () -> assertEquals(HttpStatus.NOT_FOUND, exception.getStatus())
        );
        verify(workOrderRepository, never()).save(any());
    }
}
