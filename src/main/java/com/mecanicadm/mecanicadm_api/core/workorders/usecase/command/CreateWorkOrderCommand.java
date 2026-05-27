package com.mecanicadm.mecanicadm_api.core.workorders.usecase.command;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CreateWorkOrderCommand(
        @NotNull(message = "{validation.workorder.clientId.required}")
        UUID clientId,

        @NotBlank(message = "{validation.workorder.vehicleId.required}")
        String vehicleId,

        @NotBlank(message = "{validation.workorder.description.required}")
        String description
) {
}
