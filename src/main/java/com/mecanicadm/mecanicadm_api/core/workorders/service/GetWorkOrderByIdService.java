package com.mecanicadm.mecanicadm_api.core.workorders.service;

import com.mecanicadm.mecanicadm_api.core.workorders.adapter.repository.WorkOrderRepository;
import com.mecanicadm.mecanicadm_api.core.workorders.domain.WorkOrder;
import com.mecanicadm.mecanicadm_api.core.workorders.exception.WorkOrderExceptions;
import com.mecanicadm.mecanicadm_api.core.workorders.usecase.GetWorkOrderByIdUseCase;
import com.mecanicadm.mecanicadm_api.core.workorders.usecase.query.GetWorkOrderByIdQuery;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class GetWorkOrderByIdService implements GetWorkOrderByIdUseCase {

    private final WorkOrderRepository repository;

    public GetWorkOrderByIdService(WorkOrderRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional(readOnly = true)
    public WorkOrder handle(GetWorkOrderByIdQuery query) {
        return repository.findById(query.id())
                .orElseThrow(WorkOrderExceptions.NotFound::new);
    }
}
