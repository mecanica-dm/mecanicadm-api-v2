package com.mecanicadm.mecanicadm_api.infra.features.user.api.dto;

public record AuthenticationResponse(
        String access_token,
        String userId,
        String name,
        String email
) {
}
