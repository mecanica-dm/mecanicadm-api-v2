package com.mecanicadm.mecanicadm_api.core.workorder.usecase;

import com.mecanicadm.mecanicadm_api.shared.usecase.UseCase;

import com.mecanicadm.mecanicadm_api.core.workorder.domain.port.WorkOrderFilter;
import com.mecanicadm.mecanicadm_api.core.workorder.domain.port.WorkOrderGateway;
import com.mecanicadm.mecanicadm_api.core.workorder.domain.port.WorkOrderPageQuery;
import com.mecanicadm.mecanicadm_api.core.workorder.domain.port.WorkOrderPageResult;
import com.mecanicadm.mecanicadm_api.core.workorder.usecase.query.GetAllWorkOrdersQuery;

public class GetAllWorkOrderUseCase implements UseCase<GetAllWorkOrdersQuery, WorkOrderPageResult> {

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
