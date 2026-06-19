package com.mecanicadm.mecanicadm_api.core.workorders.service;

import com.mecanicadm.mecanicadm_api.core.stockmovements.usecase.SoftDeleteStockUseCase;
import com.mecanicadm.mecanicadm_api.core.stockmovements.usecase.command.SoftDeleteStockCommand;
import com.mecanicadm.mecanicadm_api.core.workorders.adapter.repository.WorkOrderRepository;
import com.mecanicadm.mecanicadm_api.core.workorders.domain.WorkOrder;
import com.mecanicadm.mecanicadm_api.core.workorders.exception.WorkOrderExceptions;
import com.mecanicadm.mecanicadm_api.core.workorders.usecase.RemoveMaterialItemFromWorkOrderUseCase;
import com.mecanicadm.mecanicadm_api.core.workorders.usecase.command.RemoveMaterialItemFromWorkOrderCommand;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RemoveMaterialItemFromWorkOrderService implements RemoveMaterialItemFromWorkOrderUseCase {

    private final WorkOrderRepository workOrderRepository;
    private final SoftDeleteStockUseCase softDeleteStockUseCase;

    public RemoveMaterialItemFromWorkOrderService(WorkOrderRepository workOrderRepository,
                                                  SoftDeleteStockUseCase softDeleteStockUseCase) {
        this.workOrderRepository = workOrderRepository;
        this.softDeleteStockUseCase = softDeleteStockUseCase;
    }

    @Override
    @Transactional
    public void handle(RemoveMaterialItemFromWorkOrderCommand cmd) {
        WorkOrder workOrder = workOrderRepository.findById(cmd.workOrderId())
                .orElseThrow(WorkOrderExceptions.NotFound::new);

        softDeleteStockUseCase.execute(new SoftDeleteStockCommand(cmd.materialId(), cmd.workOrderId()));

        workOrder.removeMaterialItem(cmd.materialId());
        workOrderRepository.save(workOrder);
    }
}
