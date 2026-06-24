package com.mecanicadm.mecanicadm_api.infra.features.workorder.api.dto.response;

import com.mecanicadm.mecanicadm_api.core.workorders.domain.WorkOrderMaterialItem;

import java.util.UUID;

public record WorkOrderMaterialItemResponse(
        UUID materialId,
        int quantity
) {
    public WorkOrderMaterialItemResponse(WorkOrderMaterialItem materialItem) {
        this(
                materialItem.getMaterialId(),
                materialItem.getQuantity()
        );
    }
}
