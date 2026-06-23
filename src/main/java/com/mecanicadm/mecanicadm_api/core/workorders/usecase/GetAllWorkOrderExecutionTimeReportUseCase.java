package com.mecanicadm.mecanicadm_api.core.workorders.usecase;

import com.mecanicadm.mecanicadm_api.core.workorders.adapter.repository.projections.WorkOrderExecutionDurationProjection;
import com.mecanicadm.mecanicadm_api.core.workorders.adapter.repository.projections.WorkOrderExecutionSummaryProjection;
import com.mecanicadm.mecanicadm_api.core.workorders.domain.port.WorkOrderGateway;
import com.mecanicadm.mecanicadm_api.core.workorders.exception.WorkOrderExceptions;
import com.mecanicadm.mecanicadm_api.core.workorders.usecase.query.GetAllWorkOrderExecutionTimeReportQuery;
import com.mecanicadm.mecanicadm_api.infra.features.workorder.api.dto.response.WorkOrderExecutionReportResponse;
import com.mecanicadm.mecanicadm_api.infra.features.workorder.api.dto.response.WorkOrderExecutionTimeInfoResponse;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public class GetAllWorkOrderExecutionTimeReportUseCase {
    private static final String DURATION_UNIT = "minutes";

    private final WorkOrderGateway gateway;

    public GetAllWorkOrderExecutionTimeReportUseCase(WorkOrderGateway gateway) {
        this.gateway = gateway;
    }

    public WorkOrderExecutionReportResponse execute(GetAllWorkOrderExecutionTimeReportQuery query) {
        validatePeriod(query.initialDate(), query.finalDate());

        LocalDateTime initialDateTime = toInitialDateTime(query.initialDate());
        LocalDateTime finalDateTimeExclusive = toFinalDateTimeExclusive(query.finalDate());

        WorkOrderExecutionSummaryProjection summary =
                gateway.getExecutionTimeSummary(initialDateTime, finalDateTimeExclusive);

        WorkOrderExecutionTimeInfoResponse slowestWorkOrder =
                toExecutionInfo(gateway.getSlowestExecution(initialDateTime, finalDateTimeExclusive));
        WorkOrderExecutionTimeInfoResponse fastestWorkOrder =
                toExecutionInfo(gateway.getFastestExecution(initialDateTime, finalDateTimeExclusive));

        Double averageLaborExecutionTime = gateway
                .getAverageLaborExecutionMinutes(initialDateTime, finalDateTimeExclusive);

        return new WorkOrderExecutionReportResponse(
                DURATION_UNIT,
                summary != null && summary.getTotalProcessedWorkOrders() != null ? summary.getTotalProcessedWorkOrders() : 0L,
                summary != null && summary.getAverageExecutionMinutes() != null ? summary.getAverageExecutionMinutes() : 0D,
                slowestWorkOrder,
                fastestWorkOrder,
                averageLaborExecutionTime != null ? averageLaborExecutionTime : 0D
        );
    }

    private void validatePeriod(LocalDate initialDate, LocalDate finalDate) {
        if (initialDate != null && finalDate != null && initialDate.isAfter(finalDate)) {
            throw new WorkOrderExceptions.InvalidReportPeriod();
        }
    }

    private LocalDateTime toInitialDateTime(LocalDate initialDate) {
        return initialDate != null ? initialDate.atStartOfDay() : null;
    }

    private LocalDateTime toFinalDateTimeExclusive(LocalDate finalDate) {
        return finalDate != null ? finalDate.plusDays(1).atStartOfDay() : null;
    }

    private WorkOrderExecutionTimeInfoResponse toExecutionInfo(
            WorkOrderExecutionDurationProjection projection
    ) {
        if (projection == null || projection.getWorkOrderId() == null || projection.getDurationMinutes() == null) {
            return null;
        }

        UUID workOrderId = UUID.fromString(projection.getWorkOrderId().toString());
        return new WorkOrderExecutionTimeInfoResponse(workOrderId, projection.getDurationMinutes());
    }
}

