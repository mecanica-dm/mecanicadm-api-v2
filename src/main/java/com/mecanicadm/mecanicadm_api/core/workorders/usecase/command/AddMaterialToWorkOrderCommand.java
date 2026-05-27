package com.mecanicadm.mecanicadm_api.core.workorders.usecase.command;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.Positive;

import java.util.UUID;

public record AddMaterialToWorkOrderCommand(
        @JsonIgnore UUID workOrderId,
        @JsonIgnore UUID materialId,
        @Positive(message = "{validation.workorder.materialitem.quantity.positive}") int quantity
) {

    public AddMaterialToWorkOrderCommand withWorkOrderIdAndMaterialId(UUID workOrderId, UUID materialId) {
        return new AddMaterialToWorkOrderCommand(workOrderId, materialId, quantity);
    }
}
