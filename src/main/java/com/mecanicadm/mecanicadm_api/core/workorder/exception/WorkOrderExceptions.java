package com.mecanicadm.mecanicadm_api.core.workorder.exception;

import com.mecanicadm.mecanicadm_api.shared.exception.DomainExceptionCore;

public class WorkOrderExceptions {

    private WorkOrderExceptions() {
    }

    public static class NotFound extends DomainExceptionCore {
        public NotFound() {
            super("work.order.not.found", 404);
        }
    }

    public static class LaborItemNotFound extends DomainExceptionCore {
        public LaborItemNotFound() {
            super("work.order.labor.item.not.found", 404);
        }
    }

    public static class InvalidMaterialQuantity extends DomainExceptionCore {
        public InvalidMaterialQuantity() {
            super("work.order.material.quantity.invalid", 400);
        }
    }

    public static class InvalidStatusTransition extends DomainExceptionCore {
        public InvalidStatusTransition(String from, String to) {
            super("work.order.status.transition.invalid", 400, from, to);
        }
    }

    public static class InvalidLaborStatusTransition extends DomainExceptionCore {
        public InvalidLaborStatusTransition(String from, String to) {
            super("work.order.labor.status.transition.invalid", 400, from, to);
        }
    }

    public static class InvalidStatus extends DomainExceptionCore {
        public InvalidStatus(String status) {
            super("work.order.status.invalid", 400, status);
        }
    }

    public static class LaborCannotStartIfNotInExecution extends DomainExceptionCore {
        public LaborCannotStartIfNotInExecution() {
            super("work.order.labor.start.not.in.execution", 400);
        }
    }

    public static class PendingLaborItems extends DomainExceptionCore {
        public PendingLaborItems() {
            super("work.order.labor.pending.items", 400);
        }
    }

    public static class LaborItemsRequired extends DomainExceptionCore {
        public LaborItemsRequired() {
            super("work.order.labor.required", 400);
        }
    }

    public static class ClientRequired extends DomainExceptionCore {
        public ClientRequired() {
            super("work.order.client.required", 400);
        }
    }

    public static class VehicleRequired extends DomainExceptionCore {
        public VehicleRequired() {
            super("work.order.vehicle.required", 400);
        }
    }

    public static class BudgetNotFound extends DomainExceptionCore {
        public BudgetNotFound() {
            super("work.order.budget.not.found", 404);
        }
    }

    public static class BudgetDecisionInvalid extends DomainExceptionCore {
        public BudgetDecisionInvalid(String decision) {
            super("work.order.budget.decision.invalid", 400, decision);
        }
    }

    public static class BudgetRejectionReasonRequired extends DomainExceptionCore {
        public BudgetRejectionReasonRequired() {
            super("work.order.budget.rejection.reason.required", 400);
        }
    }

    public static class BudgetNotWaitingDecision extends DomainExceptionCore {
        public BudgetNotWaitingDecision() {
            super("work.order.budget.not.waiting.decision", 400);
        }
    }

    public static class InvalidReportPeriod extends DomainExceptionCore {
        public InvalidReportPeriod() {
            super("work.order.report.period.invalid", 400);
        }
    }
}
