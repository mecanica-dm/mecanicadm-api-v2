package com.mecanicadm.mecanicadm_api.core.user.usecase.query;

public record AuthenticateUserQuery(
        String email,
        String password
) {
}
