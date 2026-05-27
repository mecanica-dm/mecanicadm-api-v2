package com.mecanicadm.mecanicadm_api.core.client.usecase;

import com.mecanicadm.mecanicadm_api.core.client.usecase.command.UpdateClientCommand;

public interface UpdateClientUseCase {
    void handle(UpdateClientCommand cmd);
}
