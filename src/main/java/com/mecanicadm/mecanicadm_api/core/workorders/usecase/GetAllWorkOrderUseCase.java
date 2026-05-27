package com.mecanicadm.mecanicadm_api.core.workorders.usecase;

import com.mecanicadm.mecanicadm_api.core.workorders.domain.WorkOrder;
import com.mecanicadm.mecanicadm_api.core.workorders.usecase.query.GetAllWorkOrdersQuery;
import org.springframework.data.domain.Page;

public interface GetAllWorkOrderUseCase {
    Page<WorkOrder> handle(GetAllWorkOrdersQuery query);
}
