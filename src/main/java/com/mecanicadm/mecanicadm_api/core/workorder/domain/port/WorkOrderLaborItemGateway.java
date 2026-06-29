package com.mecanicadm.mecanicadm_api.core.workorder.domain.port;

import com.mecanicadm.mecanicadm_api.core.workorder.domain.WorkOrderLaborItem;

import java.util.Optional;
import java.util.UUID;

public interface WorkOrderLaborItemGateway {
    WorkOrderLaborItem create(WorkOrderLaborItem laborItem);

    WorkOrderLaborItem update(WorkOrderLaborItem laborItem);

    Optional<WorkOrderLaborItem> findById(UUID id);

    void delete(WorkOrderLaborItem laborItem);
}
