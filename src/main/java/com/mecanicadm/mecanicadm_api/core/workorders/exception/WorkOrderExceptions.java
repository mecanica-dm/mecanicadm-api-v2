package com.mecanicadm.mecanicadm_api.core.workorders.exception;

import com.mecanicadm.mecanicadm_api.infra.exception.DomainException;
import org.springframework.http.HttpStatus;

public class WorkOrderExceptions {

    private WorkOrderExceptions() {
    }

    public static class NotFound extends DomainException {
        public NotFound() {
            super("work.order.not.found", HttpStatus.NOT_FOUND);
        }
    }

    public static class LaborItemNotFound extends DomainException {
        public LaborItemNotFound() {
            super("work.order.labor.item.not.found", HttpStatus.NOT_FOUND);
        }
    }

    public static class InvalidMaterialQuantity extends DomainException {
        public InvalidMaterialQuantity() {
            super("work.order.material.quantity.invalid", HttpStatus.BAD_REQUEST);
        }
    }

    public static class InvalidStatusTransition extends DomainException {
        public InvalidStatusTransition(String from, String to) {
            super("work.order.status.transition.invalid", HttpStatus.BAD_REQUEST, from, to);
        }
    }

    public static class InvalidLaborStatusTransition extends DomainException {
        public InvalidLaborStatusTransition(String from, String to) {
            super("work.order.labor.status.transition.invalid", HttpStatus.BAD_REQUEST, from, to);
        }
    }

    public static class InvalidStatus extends DomainException {
        public InvalidStatus(String status) {
            super("work.order.status.invalid", HttpStatus.BAD_REQUEST, status);
        }
    }

    public static class LaborCannotStartIfNotInExecution extends DomainException {
        public LaborCannotStartIfNotInExecution() {
            super("work.order.labor.start.not.in.execution", HttpStatus.BAD_REQUEST);
        }
    }

    public static class PendingLaborItems extends DomainException {
        public PendingLaborItems() {
            super("work.order.labor.pending.items", HttpStatus.BAD_REQUEST);
        }
    }

    public static class LaborItemsRequired extends DomainException {
        public LaborItemsRequired() {
            super("work.order.labor.required", HttpStatus.BAD_REQUEST);
        }
    }

    public static class ClientRequired extends DomainException {
        public ClientRequired() {
            super("work.order.client.required", HttpStatus.BAD_REQUEST);
        }
    }

    public static class VehicleRequired extends DomainException {
        public VehicleRequired() {
            super("work.order.vehicle.required", HttpStatus.BAD_REQUEST);
        }
    }

    public static class BudgetNotFound extends DomainException {
        public BudgetNotFound() {
            super("work.order.budget.not.found", HttpStatus.NOT_FOUND);
        }
    }

    public static class BudgetDecisionInvalid extends DomainException {
        public BudgetDecisionInvalid(String decision) {
            super("work.order.budget.decision.invalid", HttpStatus.BAD_REQUEST, decision);
        }
    }

    public static class BudgetRejectionReasonRequired extends DomainException {
        public BudgetRejectionReasonRequired() {
            super("work.order.budget.rejection.reason.required", HttpStatus.BAD_REQUEST);
        }
    }

    public static class BudgetNotWaitingDecision extends DomainException {
        public BudgetNotWaitingDecision() {
            super("work.order.budget.not.waiting.decision", HttpStatus.BAD_REQUEST);
        }
    }

    public static class InvalidReportPeriod extends DomainException {
        public InvalidReportPeriod() {
            super("work.order.report.period.invalid", HttpStatus.BAD_REQUEST);
        }
    }
}
