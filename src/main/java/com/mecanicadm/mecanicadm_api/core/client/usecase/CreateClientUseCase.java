package com.mecanicadm.mecanicadm_api.core.client.usecase;

import com.mecanicadm.mecanicadm_api.core.client.usecase.command.CreateClientCommand;

import java.util.UUID;

public interface CreateClientUseCase {
    UUID handle(CreateClientCommand cmd);
}
