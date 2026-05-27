package com.mecanicadm.mecanicadm_api.core.labor.usecase.command;

import java.util.UUID;

import static java.util.Objects.requireNonNull;

public record DeleteLaborCommand(UUID id) {
    public DeleteLaborCommand {
        requireNonNull(id);
    }
}
