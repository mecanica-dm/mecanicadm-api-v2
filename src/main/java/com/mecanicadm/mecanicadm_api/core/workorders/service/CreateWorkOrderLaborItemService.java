package com.mecanicadm.mecanicadm_api.core.workorders.service;

import com.mecanicadm.mecanicadm_api.core.labor.adapter.repository.LaborRepository;
import com.mecanicadm.mecanicadm_api.core.labor.exception.LaborExceptions;
import com.mecanicadm.mecanicadm_api.core.workorders.domain.WorkOrderLaborItem;
import com.mecanicadm.mecanicadm_api.core.workorders.usecase.CreateWorkOrderLaborItemUseCase;
import com.mecanicadm.mecanicadm_api.core.workorders.usecase.command.CreateWorkOrderLaborItemCommand;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CreateWorkOrderLaborItemService implements CreateWorkOrderLaborItemUseCase {

    private final LaborRepository laborRepository;

    public CreateWorkOrderLaborItemService(LaborRepository laborRepository) {
        this.laborRepository = laborRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public WorkOrderLaborItem handle(CreateWorkOrderLaborItemCommand cmd) {
        if (!laborRepository.existsById(cmd.laborId())) {
            throw new LaborExceptions.LaborNotFound();
        }

        return WorkOrderLaborItem.create(cmd.laborId());
    }
}
