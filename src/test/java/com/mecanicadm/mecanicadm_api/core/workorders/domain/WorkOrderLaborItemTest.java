package com.mecanicadm.mecanicadm_api.core.workorders.domain;

import com.mecanicadm.mecanicadm_api.core.workorders.domain.enums.LaborExecutionStatus;
import com.mecanicadm.mecanicadm_api.core.workorders.exception.WorkOrderExceptions;
import com.mecanicadm.mecanicadm_api.infra.exception.DomainException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class WorkOrderLaborItemTest {

    @Test
    @DisplayName("Deve criar item de mão de obra com status inicial AWAITING_EXECUTION")
    void shouldCreateLaborItemWithAwaitingExecutionStatus() {
        UUID laborId = UUID.randomUUID();
        WorkOrderLaborItem item = WorkOrderLaborItem.create(laborId);

        assertEquals(laborId, item.getLaborId());
        assertEquals(LaborExecutionStatus.AWAITING_EXECUTION, item.getStatus());
        assertNull(item.getExecutionStartAt());
        assertNull(item.getExecutionEndAt());
    }

    @Test
    @DisplayName("Nao deve criar item de mão de obra com laborId nulo")
    void shouldNotCreateLaborItemWithNullLaborId() {
        assertThrows(NullPointerException.class, () -> WorkOrderLaborItem.create(null));
    }

    @Test
    @DisplayName("Deve iniciar execucao de labor quando status for AWAITING_EXECUTION")
    void shouldStartExecutionWhenStatusIsAwaitingExecution() {
        WorkOrderLaborItem item = WorkOrderLaborItem.create(UUID.randomUUID());

        item.startExecution();

        assertEquals(LaborExecutionStatus.IN_EXECUTION, item.getStatus());
        assertNotNull(item.getExecutionStartAt());
    }

    @Test
    @DisplayName("Deve lancar excecao ao iniciar execucao de labor com status diferente de AWAITING_EXECUTION")
    void shouldThrowExceptionWhenStartingExecutionWithInvalidStatus() {
        WorkOrderLaborItem item = WorkOrderLaborItem.create(UUID.randomUUID());
        item.startExecution();

        DomainException exception = assertThrows(DomainException.class, item::startExecution);

        assertAll(
                () -> assertEquals(WorkOrderExceptions.InvalidLaborStatusTransition.class, exception.getClass()),
                () -> assertEquals("work.order.labor.status.transition.invalid", exception.getMessageKey()),
                () -> assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus())
        );
    }

    @Test
    @DisplayName("Deve finalizar execucao de labor quando status for IN_EXECUTION")
    void shouldFinishExecutionWhenStatusIsInExecution() {
        WorkOrderLaborItem item = WorkOrderLaborItem.create(UUID.randomUUID());
        item.startExecution();

        item.finishExecution();

        assertEquals(LaborExecutionStatus.EXECUTION_COMPLETED, item.getStatus());
        assertNotNull(item.getExecutionEndAt());
    }

    @Test
    @DisplayName("Deve lancar excecao ao finalizar execucao de labor com status diferente de IN_EXECUTION")
    void shouldThrowExceptionWhenFinishingExecutionWithInvalidStatus() {
        WorkOrderLaborItem item = WorkOrderLaborItem.create(UUID.randomUUID());

        DomainException exception = assertThrows(DomainException.class, item::finishExecution);

        assertAll(
                () -> assertEquals(WorkOrderExceptions.InvalidLaborStatusTransition.class, exception.getClass()),
                () -> assertEquals("work.order.labor.status.transition.invalid", exception.getMessageKey()),
                () -> assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus())
        );
    }
}
