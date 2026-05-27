package com.mecanicadm.mecanicadm_api.core.workorders.service;

import com.mecanicadm.mecanicadm_api.core.workorders.adapter.repository.WorkOrderRepository;
import com.mecanicadm.mecanicadm_api.core.workorders.domain.WorkOrder;
import com.mecanicadm.mecanicadm_api.core.workorders.exception.WorkOrderExceptions;
import com.mecanicadm.mecanicadm_api.core.workorders.usecase.RemoveMaterialItemFromWorkOrderUseCase;
import com.mecanicadm.mecanicadm_api.core.workorders.usecase.SoftDeleteWorkOrderUseCase;
import com.mecanicadm.mecanicadm_api.core.workorders.usecase.command.RemoveMaterialItemFromWorkOrderCommand;
import com.mecanicadm.mecanicadm_api.core.workorders.usecase.command.SoftDeleteWorkOrderCommand;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SoftDeleteWorkOrderService implements SoftDeleteWorkOrderUseCase {

    private final WorkOrderRepository repository;
    private final RemoveMaterialItemFromWorkOrderUseCase removeMaterialItemFromWorkOrderUseCase;

    public SoftDeleteWorkOrderService(WorkOrderRepository repository,
                                      RemoveMaterialItemFromWorkOrderUseCase removeMaterialItemFromWorkOrderUseCase) {
        this.repository = repository;
        this.removeMaterialItemFromWorkOrderUseCase = removeMaterialItemFromWorkOrderUseCase;
    }

    @Override
    @Transactional
    public void handle(SoftDeleteWorkOrderCommand cmd) {
        WorkOrder workOrder = repository.findById(cmd.id()).orElseThrow(WorkOrderExceptions.NotFound::new);

        workOrder.getMaterialItems().forEach(item -> removeMaterialItemFromWorkOrderUseCase.handle(
                new RemoveMaterialItemFromWorkOrderCommand(workOrder.getId(), item.getMaterialId())));

        repository.deleteById(cmd.id());
    }
}
