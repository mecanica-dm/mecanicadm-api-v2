package com.mecanicadm.mecanicadm_api.core.user.usecase;

import com.mecanicadm.mecanicadm_api.core.user.domain.port.UserGateway;
import com.mecanicadm.mecanicadm_api.core.user.usecase.command.SoftDeleteUserCommand;

public class SoftDeleteUserUseCase {

    private final UserGateway userGateway;

    public SoftDeleteUserUseCase(UserGateway userGateway) {
        this.userGateway = userGateway;
    }

    public void execute(SoftDeleteUserCommand command) {
        userGateway.deleteById(command.id());
    }
}
