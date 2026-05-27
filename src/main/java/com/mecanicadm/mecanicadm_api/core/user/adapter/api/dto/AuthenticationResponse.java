package com.mecanicadm.mecanicadm_api.core.user.adapter.api.dto;

public record AuthenticationResponse(
        String access_token,
        String userId,
        String name,
        String email
) {
}
