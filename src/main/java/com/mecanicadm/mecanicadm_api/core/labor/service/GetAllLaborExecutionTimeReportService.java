package com.mecanicadm.mecanicadm_api.core.labor.service;

import com.mecanicadm.mecanicadm_api.core.labor.adapter.api.dto.LaborExecutionReportResponse;
import com.mecanicadm.mecanicadm_api.core.labor.adapter.api.dto.LaborTypeExecutionStatsResponse;
import com.mecanicadm.mecanicadm_api.core.labor.usecase.GetAllLaborExecutionTimeReportUseCase;
import com.mecanicadm.mecanicadm_api.core.labor.usecase.query.GetAllLaborExecutionTimeReportQuery;
import com.mecanicadm.mecanicadm_api.core.workorders.adapter.repository.WorkOrderLaborItemRepository;
import com.mecanicadm.mecanicadm_api.core.workorders.adapter.repository.projections.LaborExecutionSummaryProjection;
import com.mecanicadm.mecanicadm_api.core.workorders.adapter.repository.projections.LaborTypeStatsProjection;
import com.mecanicadm.mecanicadm_api.core.workorders.exception.WorkOrderExceptions;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class GetAllLaborExecutionTimeReportService implements GetAllLaborExecutionTimeReportUseCase {

    private static final String DURATION_UNIT = "minutes";

    private final WorkOrderLaborItemRepository workOrderLaborItemRepository;

    public GetAllLaborExecutionTimeReportService(WorkOrderLaborItemRepository workOrderLaborItemRepository) {
        this.workOrderLaborItemRepository = workOrderLaborItemRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public LaborExecutionReportResponse handle(GetAllLaborExecutionTimeReportQuery query) {
        validatePeriod(query.initialDate(), query.finalDate());

        LocalDateTime initialDateTime = toInitialDateTime(query.initialDate());
        LocalDateTime finalDateTimeExclusive = toFinalDateTimeExclusive(query.finalDate());

        LaborExecutionSummaryProjection summary =
                workOrderLaborItemRepository.getExecutionTimeSummary(initialDateTime, finalDateTimeExclusive);

        List<LaborTypeExecutionStatsResponse> statsByLaborType = workOrderLaborItemRepository
                .getStatsByLaborType(initialDateTime, finalDateTimeExclusive)
                .stream()
                .map(this::toLaborTypeStats)
                .toList();

        return new LaborExecutionReportResponse(
                DURATION_UNIT,
                summary != null && summary.getTotalProcessedLabors() != null ? summary.getTotalProcessedLabors() : 0L,
                summary != null && summary.getAverageExecutionMinutes() != null ? summary.getAverageExecutionMinutes() : 0D,
                statsByLaborType
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

    private LaborTypeExecutionStatsResponse toLaborTypeStats(LaborTypeStatsProjection projection) {
        UUID laborId = UUID.fromString(projection.getLaborId().toString());
        return new LaborTypeExecutionStatsResponse(
                laborId,
                projection.getLaborName(),
                projection.getTotalExecutions(),
                projection.getAverageExecutionMinutes(),
                projection.getMinExecutionMinutes(),
                projection.getMaxExecutionMinutes()
        );
    }
}
