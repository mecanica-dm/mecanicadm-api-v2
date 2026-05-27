package com.mecanicadm.mecanicadm_api.core.workorders.adapter.repository.projections;

public interface WorkOrderExecutionDurationProjection {
    Object getWorkOrderId();
    Double getDurationMinutes();
}
