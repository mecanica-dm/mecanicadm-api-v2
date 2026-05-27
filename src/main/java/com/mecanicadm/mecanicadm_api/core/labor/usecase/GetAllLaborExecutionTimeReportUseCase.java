package com.mecanicadm.mecanicadm_api.core.labor.usecase;

import com.mecanicadm.mecanicadm_api.core.labor.adapter.api.dto.LaborExecutionReportResponse;
import com.mecanicadm.mecanicadm_api.core.labor.usecase.query.GetAllLaborExecutionTimeReportQuery;

public interface GetAllLaborExecutionTimeReportUseCase {
    LaborExecutionReportResponse handle(GetAllLaborExecutionTimeReportQuery query);
}
