package com.mecanicadm.mecanicadm_api.core.user.usecase;

import com.mecanicadm.mecanicadm_api.core.user.usecase.command.SoftDeleteUserCommand;

public interface SoftDeleteUserUseCase {
    void handle(SoftDeleteUserCommand cmd);
}
