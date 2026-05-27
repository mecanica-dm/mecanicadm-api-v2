package com.mecanicadm.mecanicadm_api.core.labor.adapter.api.dto;

import java.util.List;

public record LaborExecutionReportResponse(
        String durationUnit,
        Long totalProcessedLabors,
        Double averageLaborExecutionTime,
        List<LaborTypeExecutionStatsResponse> statsByLaborType
) {
}
