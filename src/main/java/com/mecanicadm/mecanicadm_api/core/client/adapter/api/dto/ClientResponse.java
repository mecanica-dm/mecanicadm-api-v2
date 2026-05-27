package com.mecanicadm.mecanicadm_api.core.client.adapter.api.dto;

import com.mecanicadm.mecanicadm_api.core.client.domain.Client;

import java.util.UUID;

public record ClientResponse(
        UUID id,
        String name,
        String email,
        String document,
        String phone
) {
    public static ClientResponse fromEntity(Client client) {
        return new ClientResponse(client.getId(), client.getName(), client.getEmail(), client.getDocument(), client.getPhone());
    }
}
