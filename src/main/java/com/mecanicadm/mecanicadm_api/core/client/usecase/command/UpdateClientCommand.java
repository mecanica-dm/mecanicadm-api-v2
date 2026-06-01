package com.mecanicadm.mecanicadm_api.core.client.usecase.command;

import java.util.UUID;

public record UpdateClientCommand(
        UUID id,
        String name,
        String email,
        String document,
        String phone
) {
    public UpdateClientCommand withId(UUID id) {
        return new UpdateClientCommand(id, this.name, this.email, this.document, this.phone);
    }
}
