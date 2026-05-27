package com.mecanicadm.mecanicadm_api.core.workorders.usecase.command;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record RemoveLaborItemFromWorkOrderCommand(
        @JsonIgnore
        UUID workOrderId,

        @NotNull(message = "{validation.workorder.laboritem.id.required}")
        UUID laborItemId
) {
}
