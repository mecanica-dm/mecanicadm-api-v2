package com.mecanicadm.mecanicadm_api.core.labor.adapter.api.dto;

import java.util.UUID;

public record LaborTypeExecutionStatsResponse(
        UUID laborId,
        String laborName,
        Long totalExecutions,
        Double averageExecutionTimeInMinutes,
        Double shortestExecutionTimeInMinutes,
        Double longestExecutionTimeInMinutes
) {
}
