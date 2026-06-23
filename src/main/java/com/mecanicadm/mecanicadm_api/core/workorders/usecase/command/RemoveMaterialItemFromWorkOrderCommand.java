package com.mecanicadm.mecanicadm_api.core.workorders.usecase.command;

import java.util.UUID;

public record RemoveMaterialItemFromWorkOrderCommand(
        UUID workOrderId,
        UUID materialId
) {
}
