package com.mecanicadm.mecanicadm_api.core.workorders.usecase.query;

import org.springframework.data.domain.Pageable;

import java.util.UUID;

public record GetAllWorkOrdersQuery(
        UUID clientId,
        String licensePlate,
        Pageable pageable
) {
}
