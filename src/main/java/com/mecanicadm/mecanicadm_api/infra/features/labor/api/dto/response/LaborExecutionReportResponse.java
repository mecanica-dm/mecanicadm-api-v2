package com.mecanicadm.mecanicadm_api.infra.features.labor.api.dto.response;

import com.mecanicadm.mecanicadm_api.core.labor.domain.LaborExecutionReport;

import java.util.List;

public record LaborExecutionReportResponse(
		String durationUnit,
		Long totalProcessedLabors,
		Double averageLaborExecutionTime,
		List<LaborTypeExecutionStatsResponse> statsByLaborType
) {
    public static LaborExecutionReportResponse from(LaborExecutionReport report) {
        return new LaborExecutionReportResponse(
                report.durationUnit(),
                report.totalProcessedLabors(),
                report.averageLaborExecutionTime(),
                report.statsByLaborType().stream().map(LaborTypeExecutionStatsResponse::from).toList()
        );
    }
}

