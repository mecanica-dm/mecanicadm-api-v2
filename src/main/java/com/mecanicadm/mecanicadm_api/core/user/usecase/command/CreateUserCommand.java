package com.mecanicadm.mecanicadm_api.core.user.usecase.command;

public record CreateUserCommand(
        String email,
        String password,
        String name
) {
}
