package com.mecanicadm.mecanicadm_api.core.labor.usecase;

import com.mecanicadm.mecanicadm_api.core.labor.domain.LaborExecutionReport;
import com.mecanicadm.mecanicadm_api.core.labor.domain.LaborTypeExecutionStats;
import com.mecanicadm.mecanicadm_api.core.labor.usecase.query.GetAllLaborExecutionTimeReportQuery;
import com.mecanicadm.mecanicadm_api.core.workorders.adapter.repository.WorkOrderLaborItemRepository;
import com.mecanicadm.mecanicadm_api.core.workorders.adapter.repository.projections.LaborExecutionSummaryProjection;
import com.mecanicadm.mecanicadm_api.core.workorders.adapter.repository.projections.LaborTypeStatsProjection;
import com.mecanicadm.mecanicadm_api.core.workorders.exception.WorkOrderExceptions;
import com.mecanicadm.mecanicadm_api.shared.usecase.UseCase;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class GetAllLaborExecutionTimeReportUseCase implements UseCase<GetAllLaborExecutionTimeReportQuery, LaborExecutionReport> {

    private static final String DURATION_UNIT = "minutes";

    private final WorkOrderLaborItemRepository workOrderLaborItemRepository;

    public GetAllLaborExecutionTimeReportUseCase(WorkOrderLaborItemRepository workOrderLaborItemRepository) {
        this.workOrderLaborItemRepository = workOrderLaborItemRepository;
    }

    public LaborExecutionReport execute(GetAllLaborExecutionTimeReportQuery query) {
        validatePeriod(query.initialDate(), query.finalDate());

        LocalDateTime initialDateTime = toInitialDateTime(query.initialDate());
        LocalDateTime finalDateTimeExclusive = toFinalDateTimeExclusive(query.finalDate());

        LaborExecutionSummaryProjection summary =
                workOrderLaborItemRepository.getExecutionTimeSummary(initialDateTime, finalDateTimeExclusive);

        List<LaborTypeExecutionStats> statsByLaborType = workOrderLaborItemRepository
                .getStatsByLaborType(initialDateTime, finalDateTimeExclusive)
                .stream()
                .map(this::toLaborTypeStats)
                .toList();

        long total = summary != null && summary.getTotalProcessedLabors() != null ? summary.getTotalProcessedLabors() : 0L;
        double average = summary != null && summary.getAverageExecutionMinutes() != null ? summary.getAverageExecutionMinutes() : 0D;

        return new LaborExecutionReport(DURATION_UNIT, total, average, statsByLaborType);
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

    private LaborTypeExecutionStats toLaborTypeStats(LaborTypeStatsProjection projection) {
        UUID laborId = UUID.fromString(projection.getLaborId().toString());
        return new LaborTypeExecutionStats(
                laborId,
                projection.getLaborName(),
                projection.getTotalExecutions(),
                projection.getAverageExecutionMinutes(),
                projection.getMinExecutionMinutes(),
                projection.getMaxExecutionMinutes()
        );
    }
}
