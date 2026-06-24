package com.mecanicadm.mecanicadm_api.core.workorders.domain.port;

import com.mecanicadm.mecanicadm_api.core.workorders.domain.WorkOrder;

import java.util.List;

public record WorkOrderPageResult(
        List<WorkOrder> items, long totalElements
) {
}
