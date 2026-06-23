package com.mecanicadm.mecanicadm_api.core.workorders.domain.port;

import com.mecanicadm.mecanicadm_api.core.workorders.domain.WorkOrderLaborItem;

import java.util.Optional;
import java.util.UUID;

public interface WorkOrderLaborItemGateway {
    Optional<WorkOrderLaborItem> findById(UUID id);

    void delete(WorkOrderLaborItem laborItem);
}
