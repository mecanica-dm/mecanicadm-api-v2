package com.mecanicadm.mecanicadm_api.infra.features.workorder.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CreateWorkOrderRequest(
        @NotNull(message = "{validation.workorder.clientId.required}")
        UUID clientId,

        @NotBlank(message = "{validation.workorder.vehicleId.required}")
        String vehicleId,

        @NotBlank(message = "{validation.workorder.description.required}")
        String description
) {
}
