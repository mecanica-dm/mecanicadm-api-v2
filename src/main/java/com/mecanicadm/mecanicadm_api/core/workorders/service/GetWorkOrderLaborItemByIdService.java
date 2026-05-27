package com.mecanicadm.mecanicadm_api.core.workorders.service;

import com.mecanicadm.mecanicadm_api.core.workorders.adapter.repository.WorkOrderRepository;
import com.mecanicadm.mecanicadm_api.core.workorders.domain.WorkOrder;
import com.mecanicadm.mecanicadm_api.core.workorders.domain.WorkOrderLaborItem;
import com.mecanicadm.mecanicadm_api.core.workorders.exception.WorkOrderExceptions;
import com.mecanicadm.mecanicadm_api.core.workorders.usecase.GetWorkOrderLaborItemByIdUseCase;
import com.mecanicadm.mecanicadm_api.core.workorders.usecase.query.GetWorkOrderLaborItemByIdQuery;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class GetWorkOrderLaborItemByIdService implements GetWorkOrderLaborItemByIdUseCase {

    private final WorkOrderRepository workOrderRepository;

    public GetWorkOrderLaborItemByIdService(WorkOrderRepository workOrderRepository) {
        this.workOrderRepository = workOrderRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public WorkOrderLaborItem handle(GetWorkOrderLaborItemByIdQuery query) {
        WorkOrder workOrder = workOrderRepository.findById(query.workOrderId())
                .orElseThrow(WorkOrderExceptions.NotFound::new);

        return workOrder.findLaborItem(query.laborItemId())
                .orElseThrow(WorkOrderExceptions.LaborItemNotFound::new);
    }
}
