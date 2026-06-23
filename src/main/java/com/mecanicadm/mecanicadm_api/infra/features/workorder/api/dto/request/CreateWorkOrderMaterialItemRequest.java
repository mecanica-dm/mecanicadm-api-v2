package com.mecanicadm.mecanicadm_api.infra.features.workorder.api.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.UUID;

public record CreateWorkOrderMaterialItemRequest(
        @NotNull(message = "{validation.workorder.id.required}")
        UUID workOrderId,

        @NotNull(message = "{validation.workorder.materialitem.id.required}")
        UUID materialId,

        @Positive(message = "{validation.workorder.materialitem.quantity.positive}")
        @NotNull(message = "{validation.workorder.materialitem.quantity.required}")
        int quantity
) {
}
