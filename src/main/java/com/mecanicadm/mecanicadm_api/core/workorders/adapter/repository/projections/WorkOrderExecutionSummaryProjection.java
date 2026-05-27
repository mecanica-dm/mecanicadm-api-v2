package com.mecanicadm.mecanicadm_api.core.workorders.adapter.repository.projections;

public interface WorkOrderExecutionSummaryProjection {
    Long getTotalProcessedWorkOrders();
    Double getAverageExecutionMinutes();
}
