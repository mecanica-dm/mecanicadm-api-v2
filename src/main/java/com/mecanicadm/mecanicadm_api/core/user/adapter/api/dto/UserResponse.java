package com.mecanicadm.mecanicadm_api.core.user.adapter.api.dto;

import com.mecanicadm.mecanicadm_api.core.user.domain.User;

public record UserResponse(
        String email,
        String name
) {
    public static UserResponse fromEntity(User user) {
        return new UserResponse(user.getEmail(), user.getName());
    }
}