package com.mecanicadm.mecanicadm_api.core.workorders.usecase.command;

import java.util.UUID;

public record CreateWorkOrderMaterialItemCommand(
        UUID workOrderId,
        UUID materialId,
        int quantity
) {
}
