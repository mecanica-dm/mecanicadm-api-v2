package com.mecanicadm.mecanicadm_api.core.workorders.usecase.command;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record AddLaborToWorkOrderCommand(
        @JsonIgnore
        UUID workOrderId,

        @NotNull(message = "{validation.workorder.labor.id.required}")
        UUID laborId
) {
}
