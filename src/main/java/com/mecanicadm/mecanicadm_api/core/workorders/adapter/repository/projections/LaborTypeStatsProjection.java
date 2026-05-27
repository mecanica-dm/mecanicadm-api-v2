package com.mecanicadm.mecanicadm_api.core.workorders.adapter.repository.projections;

public interface LaborTypeStatsProjection {
    Object getLaborId();
    String getLaborName();
    Long getTotalExecutions();
    Double getAverageExecutionMinutes();
    Double getMinExecutionMinutes();
    Double getMaxExecutionMinutes();
}
