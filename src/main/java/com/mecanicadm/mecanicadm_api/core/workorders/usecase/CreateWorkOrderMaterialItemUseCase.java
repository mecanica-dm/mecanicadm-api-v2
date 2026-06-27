package com.mecanicadm.mecanicadm_api.core.workorders.usecase;

import com.mecanicadm.mecanicadm_api.core.material.domain.Material;
import com.mecanicadm.mecanicadm_api.core.material.domain.port.MaterialGateway;
import com.mecanicadm.mecanicadm_api.core.material.exception.MaterialExceptions;
import com.mecanicadm.mecanicadm_api.core.stockmovements.usecase.DeductStockUseCase;
import com.mecanicadm.mecanicadm_api.core.stockmovements.usecase.command.DeductStockCommand;
import com.mecanicadm.mecanicadm_api.core.workorders.domain.WorkOrderMaterialItem;
import com.mecanicadm.mecanicadm_api.core.workorders.usecase.command.CreateWorkOrderMaterialItemCommand;

public class CreateWorkOrderMaterialItemUseCase {
    private final MaterialGateway materialGateway;
    private final DeductStockUseCase deductStockUseCase;

    public CreateWorkOrderMaterialItemUseCase(MaterialGateway materialGateway, DeductStockUseCase deductStockUseCase) {
        this.materialGateway = materialGateway;
        this.deductStockUseCase = deductStockUseCase;
    }

    public WorkOrderMaterialItem execute(CreateWorkOrderMaterialItemCommand cmd) {
        Material material = materialGateway.findById(cmd.materialId())
                .orElseThrow(MaterialExceptions.MaterialNotFound::new);

        deductStockUseCase.execute(new DeductStockCommand(material.getId(), cmd.workOrderId(), cmd.quantity()));

        return WorkOrderMaterialItem.create(material.getId(), cmd.quantity());
    }
}
