package com.mecanicadm.mecanicadm_api.core.client.usecase;

import com.mecanicadm.mecanicadm_api.core.client.usecase.command.SoftDeleteClientCommand;

public interface SoftDeleteClientUseCase {
    void handle(SoftDeleteClientCommand cmd);
}
