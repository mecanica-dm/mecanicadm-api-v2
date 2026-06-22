package com.mecanicadm.mecanicadm_api.core.user.domain;

import java.util.UUID;

public record UserAuthentication(String token, UUID userId, String name, String email) {
}
