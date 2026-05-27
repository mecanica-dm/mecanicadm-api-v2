package com.mecanicadm.mecanicadm_api.core.workorders.service;

import com.mecanicadm.mecanicadm_api.core.workorders.adapter.repository.WorkOrderRepository;
import com.mecanicadm.mecanicadm_api.core.workorders.exception.WorkOrderExceptions;
import com.mecanicadm.mecanicadm_api.core.workorders.usecase.UpdateWorkOrderUseCase;
import com.mecanicadm.mecanicadm_api.core.workorders.usecase.command.UpdateWorkOrderCommand;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UpdateWorkOrderService implements UpdateWorkOrderUseCase {

    private final WorkOrderRepository repository;

    public UpdateWorkOrderService(WorkOrderRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional
    public void handle(UpdateWorkOrderCommand cmd) {
        var workOrder = repository.findById(cmd.id())
                .orElseThrow(WorkOrderExceptions.NotFound::new);

        workOrder.update(cmd.clientId(), cmd.vehicleId(), cmd.description());

        repository.save(workOrder);
    }
}
