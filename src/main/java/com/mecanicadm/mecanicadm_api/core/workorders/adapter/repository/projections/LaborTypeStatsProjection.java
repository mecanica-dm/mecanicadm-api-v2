package com.mecanicadm.mecanicadm_api.core.workorders.adapter.repository.projections;

import java.util.UUID;

public interface LaborTypeStatsProjection {
    UUID getLaborId();
    String getLaborName();
    Long getTotalExecutions();
    Double getAverageExecutionMinutes();
    Double getMinExecutionMinutes();
    Double getMaxExecutionMinutes();
}
