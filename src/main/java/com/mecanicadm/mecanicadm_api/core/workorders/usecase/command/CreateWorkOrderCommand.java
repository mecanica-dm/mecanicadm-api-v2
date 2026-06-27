package com.mecanicadm.mecanicadm_api.core.workorders.usecase.command;

import java.util.UUID;

public record CreateWorkOrderCommand(
        UUID clientId,
        String vehicleId,
        String description
) {
}
