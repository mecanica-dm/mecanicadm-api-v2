package com.mecanicadm.mecanicadm_api.core.workorder.domain.port;

import java.util.UUID;

public record WorkOrderFilter(UUID clientId, String licensePlate) {
}
