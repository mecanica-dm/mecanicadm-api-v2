package com.mecanicadm.mecanicadm_api.core.user.usecase;

import com.mecanicadm.mecanicadm_api.core.user.usecase.command.ResetPasswordCommand;

public interface ResetPasswordUseCase {
    void handle(ResetPasswordCommand cmd);
}
