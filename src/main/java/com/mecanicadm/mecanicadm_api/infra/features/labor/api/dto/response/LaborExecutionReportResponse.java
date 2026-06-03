package com.mecanicadm.mecanicadm_api.infra.features.labor.api.dto.response;

import java.util.List;

public record LaborExecutionReportResponse(
		String durationUnit,
		Long totalProcessedLabors,
		Double averageLaborExecutionTime,
		List<LaborTypeExecutionStatsResponse> statsByLaborType
) {
	// Response DTO for labor execution report. Built by infra or use-case layer.
}

