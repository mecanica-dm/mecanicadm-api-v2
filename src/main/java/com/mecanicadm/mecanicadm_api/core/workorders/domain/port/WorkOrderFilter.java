package com.mecanicadm.mecanicadm_api.core.workorders.domain.port;

import java.util.UUID;

public record WorkOrderFilter(UUID clientId, String licensePlate) {
}
