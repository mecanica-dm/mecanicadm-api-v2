package com.mecanicadm.mecanicadm_api.core.workorders.usecase.command;

import java.util.UUID;

public record AddMaterialToWorkOrderCommand(
        UUID workOrderId,
        UUID materialId,
        int quantity
) {

    public AddMaterialToWorkOrderCommand withWorkOrderIdAndMaterialId(UUID workOrderId, UUID materialId) {
        return new AddMaterialToWorkOrderCommand(workOrderId, materialId, quantity);
    }
}
