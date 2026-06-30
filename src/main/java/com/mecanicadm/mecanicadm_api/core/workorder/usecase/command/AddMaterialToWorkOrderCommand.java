package com.mecanicadm.mecanicadm_api.core.workorder.usecase.command;

import java.util.UUID;

public record AddMaterialToWorkOrderCommand(
        UUID workOrderId,
        UUID materialId,
        int quantity
) {
}
