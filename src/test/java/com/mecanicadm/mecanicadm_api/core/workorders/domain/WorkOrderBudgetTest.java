package com.mecanicadm.mecanicadm_api.core.workorders.domain;

import com.mecanicadm.mecanicadm_api.core.workorders.domain.enums.WorkOrderBudgetStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class WorkOrderBudgetTest {

    @Test
    @DisplayName("Deve criar orcamento com status inicial PENDING")
    void shouldCreateBudgetWithWaitingApprovalStatus() {
        WorkOrder workOrder = WorkOrder.create(UUID.randomUUID(), "VEH-001", "Servico");
        WorkOrderBudget budget = WorkOrderBudget.create(workOrder, new BigDecimal("100.00"));

        assertEquals(new BigDecimal("100.00"), budget.getTotalPrice());
        assertEquals(WorkOrderBudgetStatus.PENDING, budget.getStatus());
    }

    @Test
    @DisplayName("Deve atualizar total do orcamento e voltar para PENDING")
    void shouldUpdateBudgetTotalPriceAndResetStatus() {
        WorkOrder workOrder = WorkOrder.create(UUID.randomUUID(), "VEH-001", "Servico");
        WorkOrderBudget budget = WorkOrderBudget.create(workOrder, new BigDecimal("100.00"));

        budget.updateStatus(WorkOrderBudgetStatus.APPROVED);

        budget.updateTotalPrice(new BigDecimal("150.00"));

        assertEquals(new BigDecimal("150.00"), budget.getTotalPrice());
        assertEquals(WorkOrderBudgetStatus.PENDING, budget.getStatus());
    }

    @Test
    @DisplayName("Deve atualizar status do orcamento")
    void shouldUpdateBudgetStatus() {
        WorkOrder workOrder = WorkOrder.create(UUID.randomUUID(), "VEH-001", "Servico");
        WorkOrderBudget budget = WorkOrderBudget.create(workOrder, new BigDecimal("100.00"));

        budget.updateStatus(WorkOrderBudgetStatus.CHANGES_REQUESTED);

        assertEquals(WorkOrderBudgetStatus.CHANGES_REQUESTED, budget.getStatus());
    }

    @Test
    @DisplayName("Deve marcar orcamento como enviado")
    void shouldMarkBudgetAsSent() {
        WorkOrder workOrder = WorkOrder.create(UUID.randomUUID(), "VEH-001", "Servico");
        WorkOrderBudget budget = WorkOrderBudget.create(workOrder, new BigDecimal("100.00"));

        budget.send();

        assertEquals(WorkOrderBudgetStatus.WAITING_DECISION, budget.getStatus());
    }

    @Test
    @DisplayName("Nao deve criar orcamento com ordem de servico nula")
    void shouldNotCreateBudgetWithNullWorkOrder() {
        BigDecimal amount = new BigDecimal("100.00");

        assertThrows(
                NullPointerException.class,
                () -> WorkOrderBudget.create(null, amount)
        );
    }

    @Test
    @DisplayName("Nao deve criar orcamento com valor total nulo")
    void shouldNotCreateBudgetWithNullTotalPrice() {
        WorkOrder workOrder = WorkOrder.create(UUID.randomUUID(), "VEH-001", "Servico");
        assertThrows(NullPointerException.class, () -> WorkOrderBudget.create(workOrder, null));
    }

    @Test
    @DisplayName("Nao deve atualizar valor total para nulo")
    void shouldNotUpdateTotalPriceToNull() {
        WorkOrder workOrder = WorkOrder.create(UUID.randomUUID(), "VEH-001", "Servico");
        WorkOrderBudget budget = WorkOrderBudget.create(workOrder, new BigDecimal("100.00"));

        assertThrows(NullPointerException.class, () -> budget.updateTotalPrice(null));
    }

    @Test
    @DisplayName("Nao deve atualizar status para nulo")
    void shouldNotUpdateStatusToNull() {
        WorkOrder workOrder = WorkOrder.create(UUID.randomUUID(), "VEH-001", "Servico");
        WorkOrderBudget budget = WorkOrderBudget.create(workOrder, new BigDecimal("100.00"));

        assertThrows(NullPointerException.class, () -> budget.updateStatus(null));
    }
}
