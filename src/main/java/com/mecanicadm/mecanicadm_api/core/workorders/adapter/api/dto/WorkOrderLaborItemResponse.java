package com.mecanicadm.mecanicadm_api.core.workorders.adapter.api.dto;

import com.mecanicadm.mecanicadm_api.core.workorders.domain.WorkOrderLaborItem;
import com.mecanicadm.mecanicadm_api.core.workorders.domain.enums.LaborExecutionStatus;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;

public record WorkOrderLaborItemResponse(
        UUID id,
        UUID laborId,
        LaborExecutionStatus status,
        LocalDateTime executionStartAt,
        LocalDateTime executionEndAt,
        Long totalExecutionTimeInMinutes
) {
    public WorkOrderLaborItemResponse(WorkOrderLaborItem laborItem) {
        this(
                laborItem.getId(),
                laborItem.getLaborId(),
                laborItem.getStatus(),
                laborItem.getExecutionStartAt(),
                laborItem.getExecutionEndAt(),
                calculateTotalExecutionTime(laborItem.getExecutionStartAt(), laborItem.getExecutionEndAt())
        );
    }

    private static Long calculateTotalExecutionTime(LocalDateTime start, LocalDateTime end) {
        if (start == null || end == null) {
            return 0L;
        }
        return Duration.between(start, end).toMinutes();
    }
}
