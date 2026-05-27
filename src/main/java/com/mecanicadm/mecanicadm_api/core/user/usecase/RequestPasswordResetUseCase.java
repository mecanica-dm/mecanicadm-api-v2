package com.mecanicadm.mecanicadm_api.core.user.usecase;

import com.mecanicadm.mecanicadm_api.core.user.usecase.command.RequestPasswordResetCommand;

public interface RequestPasswordResetUseCase {
    void handle(RequestPasswordResetCommand cmd);
}
