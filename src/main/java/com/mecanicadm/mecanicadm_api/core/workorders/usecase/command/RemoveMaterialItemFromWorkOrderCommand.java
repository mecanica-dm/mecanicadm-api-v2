package com.mecanicadm.mecanicadm_api.core.workorders.usecase.command;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.UUID;

public record RemoveMaterialItemFromWorkOrderCommand(
        @JsonIgnore
        UUID workOrderId,

        @JsonIgnore
        UUID materialId
) {
}
