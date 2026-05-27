package com.mecanicadm.mecanicadm_api.core.workorders.exception;

import com.mecanicadm.mecanicadm_api.infra.exception.DomainException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class WorkOrderExceptionsTest {

    @Test
    @DisplayName("Deve cobrir o construtor privado de WorkOrderExceptions")
    void shouldCoverPrivateConstructor() throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Constructor<WorkOrderExceptions> constructor = WorkOrderExceptions.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        WorkOrderExceptions instance = constructor.newInstance();
        assertNotNull(instance);
    }

    @Test
    @DisplayName("Deve instanciar todas as exceções de WorkOrder para garantir cobertura")
    void shouldInstantiateAllExceptions() {
        assertException(new WorkOrderExceptions.NotFound(), HttpStatus.NOT_FOUND, "work.order.not.found");
        assertException(new WorkOrderExceptions.InvalidMaterialQuantity(), HttpStatus.BAD_REQUEST, "work.order.material.quantity.invalid");
        assertException(new WorkOrderExceptions.InvalidStatusTransition("FROM", "TO"), HttpStatus.BAD_REQUEST, "work.order.status.transition.invalid");
        assertException(new WorkOrderExceptions.LaborItemsRequired(), HttpStatus.BAD_REQUEST, "work.order.labor.required");
        assertException(new WorkOrderExceptions.LaborItemNotFound(), HttpStatus.NOT_FOUND, "work.order.labor.item.not.found");
        assertException(new WorkOrderExceptions.ClientRequired(), HttpStatus.BAD_REQUEST, "work.order.client.required");
        assertException(new WorkOrderExceptions.VehicleRequired(), HttpStatus.BAD_REQUEST, "work.order.vehicle.required");
        assertException(new WorkOrderExceptions.BudgetNotFound(), HttpStatus.NOT_FOUND, "work.order.budget.not.found");
        assertException(new WorkOrderExceptions.BudgetDecisionInvalid("INVALID_DECISION"), HttpStatus.BAD_REQUEST, "work.order.budget.decision.invalid");
        assertException(new WorkOrderExceptions.BudgetRejectionReasonRequired(), HttpStatus.BAD_REQUEST, "work.order.budget.rejection.reason.required");
        assertException(new WorkOrderExceptions.BudgetNotWaitingDecision(), HttpStatus.BAD_REQUEST, "work.order.budget.not.waiting.decision");
        assertException(new WorkOrderExceptions.InvalidLaborStatusTransition("FROM", "TO"), HttpStatus.BAD_REQUEST, "work.order.labor.status.transition.invalid");
        assertException(new WorkOrderExceptions.LaborCannotStartIfNotInExecution(), HttpStatus.BAD_REQUEST, "work.order.labor.start.not.in.execution");
        assertException(new WorkOrderExceptions.InvalidReportPeriod(), HttpStatus.BAD_REQUEST, "work.order.report.period.invalid");
    }

    private void assertException(DomainException ex, HttpStatus status, String messageKey) {
        assertEquals(status, ex.getStatus());
        assertEquals(messageKey, ex.getMessageKey());
    }
}
