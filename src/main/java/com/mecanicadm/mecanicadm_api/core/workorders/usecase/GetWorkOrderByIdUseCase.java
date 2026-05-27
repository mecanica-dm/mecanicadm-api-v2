package com.mecanicadm.mecanicadm_api.core.workorders.usecase;

import com.mecanicadm.mecanicadm_api.core.workorders.domain.WorkOrder;
import com.mecanicadm.mecanicadm_api.core.workorders.usecase.query.GetWorkOrderByIdQuery;

public interface GetWorkOrderByIdUseCase {
    WorkOrder handle(GetWorkOrderByIdQuery query);
}
