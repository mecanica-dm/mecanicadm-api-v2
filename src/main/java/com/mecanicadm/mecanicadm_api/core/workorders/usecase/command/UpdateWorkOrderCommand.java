package com.mecanicadm.mecanicadm_api.core.workorders.usecase.command;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.UUID;

public record UpdateWorkOrderCommand(
        @JsonIgnore
        UUID id,
        String vehicleId,
        UUID clientId,
        String description
) {
    public UpdateWorkOrderCommand withId(UUID id) {
        return new UpdateWorkOrderCommand(id, vehicleId, clientId, description);
    }
}
