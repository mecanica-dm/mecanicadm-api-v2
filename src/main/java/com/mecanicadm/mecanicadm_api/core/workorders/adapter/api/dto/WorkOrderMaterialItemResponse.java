package com.mecanicadm.mecanicadm_api.core.workorders.adapter.api.dto;

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
