package com.mecanicadm.mecanicadm_api.core.user.usecase;

import com.mecanicadm.mecanicadm_api.core.user.usecase.command.CreateUserCommand;

import java.util.UUID;

public interface CreateUserUseCase {
    UUID handle(CreateUserCommand cmd);
}
