package com.mecanicadm.mecanicadm_api.core.workorders.adapter.api.dto;

import java.util.UUID;

public record WorkOrderExecutionTimeInfoResponse(
        UUID workOrderId,
        Double durationInMinutes
) {
}

