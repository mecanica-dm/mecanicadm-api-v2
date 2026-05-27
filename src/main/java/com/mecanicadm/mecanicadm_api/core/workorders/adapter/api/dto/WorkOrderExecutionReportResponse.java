package com.mecanicadm.mecanicadm_api.core.workorders.adapter.api.dto;

public record WorkOrderExecutionReportResponse(
        String durationUnit,
        Long totalProcessedWorkOrders,
        Double averageWorkOrderExecutionTime,
        WorkOrderExecutionTimeInfoResponse slowestWorkOrder,
        WorkOrderExecutionTimeInfoResponse fastestWorkOrder,
        Double averageWorkOrderLaborExecutionTime
) {
}

