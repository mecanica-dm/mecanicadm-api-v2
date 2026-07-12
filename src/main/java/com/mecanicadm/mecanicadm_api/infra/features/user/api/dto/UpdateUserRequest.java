package com.mecanicadm.mecanicadm_api.infra.features.user.api.dto;

import jakarta.validation.constraints.Size;

public record UpdateUserRequest(
        @Size(max = 255) String name,
        @Size(min = 6, max = 255) String password,
        @Size(max = 255) String currentPassword
) {}
