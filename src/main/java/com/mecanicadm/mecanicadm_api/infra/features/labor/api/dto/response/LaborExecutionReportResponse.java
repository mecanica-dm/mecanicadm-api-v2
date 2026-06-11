package com.mecanicadm.mecanicadm_api.infra.features.labor.api.dto.response;

import java.util.List;

public record LaborExecutionReportResponse(
		String durationUnit,
		Long totalProcessedLabors,
		Double averageLaborExecutionTime,
		List<LaborTypeExecutionStatsResponse> statsByLaborType
) {
}

