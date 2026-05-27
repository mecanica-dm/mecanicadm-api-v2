package com.mecanicadm.mecanicadm_api.core.workorders.adapter.repository.projections;

public interface LaborExecutionSummaryProjection {
    Long getTotalProcessedLabors();
    Double getAverageExecutionMinutes();
}
