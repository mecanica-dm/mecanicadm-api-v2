package com.mecanicadm.mecanicadm_api.core.workorders.service;

import com.mecanicadm.mecanicadm_api.core.workorders.adapter.repository.WorkOrderLaborItemRepository;
import com.mecanicadm.mecanicadm_api.core.workorders.domain.WorkOrderLaborItem;
import com.mecanicadm.mecanicadm_api.core.workorders.exception.WorkOrderExceptions;
import com.mecanicadm.mecanicadm_api.core.workorders.usecase.RemoveLaborItemFromWorkOrderUseCase;
import com.mecanicadm.mecanicadm_api.core.workorders.usecase.command.RemoveLaborItemFromWorkOrderCommand;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RemoveLaborItemFromWorkOrderService implements RemoveLaborItemFromWorkOrderUseCase {

    private final WorkOrderLaborItemRepository repository;

    public RemoveLaborItemFromWorkOrderService(WorkOrderLaborItemRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional
    public void handle(RemoveLaborItemFromWorkOrderCommand cmd) {
        WorkOrderLaborItem laborItem = repository.findById(cmd.laborItemId())
                .orElseThrow(WorkOrderExceptions.LaborItemNotFound::new);

        repository.delete(laborItem);
    }
}
