package com.mecanicadm.mecanicadm_api.core.workorder.usecase;

import com.mecanicadm.mecanicadm_api.shared.usecase.UseCase;

import com.mecanicadm.mecanicadm_api.core.workorder.domain.WorkOrder;
import com.mecanicadm.mecanicadm_api.core.workorder.domain.port.WorkOrderGateway;
import com.mecanicadm.mecanicadm_api.core.workorder.exception.WorkOrderExceptions;
import com.mecanicadm.mecanicadm_api.core.workorder.usecase.query.GetWorkOrderByIdQuery;

public class GetWorkOrderByIdUseCase implements UseCase<GetWorkOrderByIdQuery, WorkOrder> {
    private final WorkOrderGateway gateway;

    public GetWorkOrderByIdUseCase(WorkOrderGateway gateway) {
        this.gateway = gateway;
    }

    public WorkOrder execute(GetWorkOrderByIdQuery query) {
        return gateway.findById(query.id())
                .orElseThrow(WorkOrderExceptions.NotFound::new);
    }
}
