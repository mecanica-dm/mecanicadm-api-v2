package com.mecanicadm.mecanicadm_api.core.workorders.usecase.command;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CreateWorkOrderLaborItemCommand(
        @NotNull(message = "{validation.workorder.laborId.required}")
        UUID laborId
) {
}
