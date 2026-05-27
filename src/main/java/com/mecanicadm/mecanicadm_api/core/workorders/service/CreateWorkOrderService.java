package com.mecanicadm.mecanicadm_api.core.workorders.service;

import com.mecanicadm.mecanicadm_api.core.workorders.adapter.repository.WorkOrderRepository;
import com.mecanicadm.mecanicadm_api.core.workorders.domain.WorkOrder;
import com.mecanicadm.mecanicadm_api.core.workorders.usecase.CreateWorkOrderUseCase;
import com.mecanicadm.mecanicadm_api.core.workorders.usecase.command.CreateWorkOrderCommand;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class CreateWorkOrderService implements CreateWorkOrderUseCase {

    private final WorkOrderRepository repository;

    public CreateWorkOrderService(WorkOrderRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional
    public UUID handle(CreateWorkOrderCommand cmd) {
        WorkOrder workOrder = WorkOrder.create(cmd.clientId(), cmd.vehicleId(), cmd.description());
        repository.save(workOrder);
        return workOrder.getId();
    }
}
