package com.mecanicadm.mecanicadm_api.core.workorders.usecase;

import com.mecanicadm.mecanicadm_api.core.workorders.domain.WorkOrder;
import com.mecanicadm.mecanicadm_api.core.workorders.domain.WorkOrderLaborItem;
import com.mecanicadm.mecanicadm_api.core.workorders.domain.port.WorkOrderLaborItemGateway;
import com.mecanicadm.mecanicadm_api.core.workorders.exception.WorkOrderExceptions;
import com.mecanicadm.mecanicadm_api.core.workorders.usecase.query.GetWorkOrderLaborItemByIdQuery;

public class GetWorkOrderLaborItemByIdUseCase {
    private final WorkOrderLaborItemGateway gateway;

    public GetWorkOrderLaborItemByIdUseCase(WorkOrderLaborItemGateway gateway) {
        this.gateway = gateway;
    }

    public WorkOrderLaborItem execute(GetWorkOrderLaborItemByIdQuery query) {
        WorkOrder workOrder = gateway.findById(query.workOrderId())
                .orElseThrow(WorkOrderExceptions.NotFound::new);

        return workOrder.findLaborItem(query.laborItemId())
                .orElseThrow(WorkOrderExceptions.LaborItemNotFound::new);
    }
}
