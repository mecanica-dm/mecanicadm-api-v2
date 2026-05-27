package com.mecanicadm.mecanicadm_api.core.user.usecase;

import com.mecanicadm.mecanicadm_api.core.user.usecase.command.UpdateUserCommand;

public interface UpdateUserUseCase {
    void handle(UpdateUserCommand cmd);
}
