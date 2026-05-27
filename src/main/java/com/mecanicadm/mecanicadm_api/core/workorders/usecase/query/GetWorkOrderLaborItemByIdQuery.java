package com.mecanicadm.mecanicadm_api.core.workorders.usecase.query;

import java.util.UUID;

public record GetWorkOrderLaborItemByIdQuery(UUID workOrderId, UUID laborItemId) { }
