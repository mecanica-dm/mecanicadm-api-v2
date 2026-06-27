package com.mecanicadm.mecanicadm_api.core.workorders.usecase;

import com.mecanicadm.mecanicadm_api.core.workorders.domain.port.WorkOrderFilter;
import com.mecanicadm.mecanicadm_api.core.workorders.domain.port.WorkOrderGateway;
import com.mecanicadm.mecanicadm_api.core.workorders.domain.port.WorkOrderPageQuery;
import com.mecanicadm.mecanicadm_api.core.workorders.domain.port.WorkOrderPageResult;
import com.mecanicadm.mecanicadm_api.core.workorders.usecase.query.GetAllWorkOrdersQuery;

public class GetAllWorkOrderUseCase {

    private final WorkOrderGateway gateway;

    public GetAllWorkOrderUseCase(WorkOrderGateway gateway) {
        this.gateway = gateway;
    }

    public WorkOrderPageResult execute(GetAllWorkOrdersQuery query) {
        WorkOrderFilter filter = new WorkOrderFilter(query.clientId(), query.licensePlate());
        WorkOrderPageQuery pageQuery = new WorkOrderPageQuery(
                filter, query.page(), query.size(), query.sortBy(), query.direction());
        return gateway.findAll(pageQuery);
    }
}
