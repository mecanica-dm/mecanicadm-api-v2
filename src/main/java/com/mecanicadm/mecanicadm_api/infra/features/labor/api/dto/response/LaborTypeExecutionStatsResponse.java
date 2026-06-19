package com.mecanicadm.mecanicadm_api.infra.features.labor.api.dto.response;

import com.mecanicadm.mecanicadm_api.core.workorders.adapter.repository.projections.LaborTypeStatsProjection;

import java.util.UUID;

public record LaborTypeExecutionStatsResponse(
        UUID laborId,
        String laborName,
        Long totalExecutions,
        Double averageExecutionTimeInMinutes,
        Double shortestExecutionTimeInMinutes,
        Double longestExecutionTimeInMinutes
) {

    public LaborTypeExecutionStatsResponse(LaborTypeStatsProjection proj) {
        this(
                proj.getLaborId(),
                proj.getLaborName(),
                proj.getTotalExecutions(),
                proj.getAverageExecutionMinutes(),
                proj.getMinExecutionMinutes(),
                proj.getMaxExecutionMinutes()
        );
    }
}

