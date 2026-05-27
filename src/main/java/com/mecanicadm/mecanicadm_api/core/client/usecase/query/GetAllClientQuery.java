package com.mecanicadm.mecanicadm_api.core.client.usecase.query;

import org.springframework.data.domain.Pageable;

public record GetAllClientQuery(
        String name,
        String document,
        Pageable pageable
) {
}
