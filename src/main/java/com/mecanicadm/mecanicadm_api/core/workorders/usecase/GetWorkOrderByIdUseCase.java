package com.mecanicadm.mecanicadm_api.core.workorders.usecase;

import com.mecanicadm.mecanicadm_api.core.workorders.domain.WorkOrder;
import com.mecanicadm.mecanicadm_api.core.workorders.domain.port.WorkOrderGateway;
import com.mecanicadm.mecanicadm_api.core.workorders.exception.WorkOrderExceptions;
import com.mecanicadm.mecanicadm_api.core.workorders.usecase.query.GetWorkOrderByIdQuery;

public class GetWorkOrderByIdUseCase {
    private final WorkOrderGateway gateway;

    public GetWorkOrderByIdUseCase(WorkOrderGateway gateway) {
        this.gateway = gateway;
    }

    public WorkOrder execute(GetWorkOrderByIdQuery query) {
        return gateway.findById(query.id())
                .orElseThrow(WorkOrderExceptions.NotFound::new);
    }
}
