package com.mecanicadm.mecanicadm_api.core.workorders.service;

import com.mecanicadm.mecanicadm_api.core.workorders.adapter.api.dto.WorkOrderExecutionTimeInfoResponse;
import com.mecanicadm.mecanicadm_api.core.workorders.adapter.api.dto.WorkOrderExecutionReportResponse;
import com.mecanicadm.mecanicadm_api.core.workorders.adapter.repository.WorkOrderRepository;
import com.mecanicadm.mecanicadm_api.core.workorders.adapter.repository.projections.WorkOrderExecutionDurationProjection;
import com.mecanicadm.mecanicadm_api.core.workorders.adapter.repository.projections.WorkOrderExecutionSummaryProjection;
import com.mecanicadm.mecanicadm_api.core.workorders.exception.WorkOrderExceptions;
import com.mecanicadm.mecanicadm_api.core.workorders.usecase.GetAllWorkOrderExecutionTimeReportUseCase;
import com.mecanicadm.mecanicadm_api.core.workorders.usecase.query.GetAllWorkOrderExecutionTimeReportQuery;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class GetAllWorkOrderExecutionTimeReportService implements GetAllWorkOrderExecutionTimeReportUseCase {

    private static final String DURATION_UNIT = "minutes";

    private final WorkOrderRepository workOrderRepository;

    public GetAllWorkOrderExecutionTimeReportService(WorkOrderRepository workOrderRepository) {
        this.workOrderRepository = workOrderRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public WorkOrderExecutionReportResponse handle(GetAllWorkOrderExecutionTimeReportQuery query) {
        validatePeriod(query.initialDate(), query.finalDate());

        LocalDateTime initialDateTime = toInitialDateTime(query.initialDate());
        LocalDateTime finalDateTimeExclusive = toFinalDateTimeExclusive(query.finalDate());

        WorkOrderExecutionSummaryProjection summary =
                workOrderRepository.getExecutionTimeSummary(initialDateTime, finalDateTimeExclusive);

        WorkOrderExecutionTimeInfoResponse slowestWorkOrder =
                toExecutionInfo(workOrderRepository.getSlowestExecution(initialDateTime, finalDateTimeExclusive));
        WorkOrderExecutionTimeInfoResponse fastestWorkOrder =
                toExecutionInfo(workOrderRepository.getFastestExecution(initialDateTime, finalDateTimeExclusive));

        Double averageLaborExecutionTime = workOrderRepository
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
