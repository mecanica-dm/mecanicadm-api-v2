package com.mecanicadm.mecanicadm_api.core.workorders.usecase;

import com.mecanicadm.mecanicadm_api.core.workorders.domain.WorkOrderLaborItem;
import com.mecanicadm.mecanicadm_api.core.workorders.usecase.query.GetWorkOrderLaborItemByIdQuery;

public interface GetWorkOrderLaborItemByIdUseCase {
    WorkOrderLaborItem handle(GetWorkOrderLaborItemByIdQuery query);
}
