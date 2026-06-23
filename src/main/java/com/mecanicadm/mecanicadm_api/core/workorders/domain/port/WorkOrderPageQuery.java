package com.mecanicadm.mecanicadm_api.core.workorders.domain.port;

public record WorkOrderPageQuery(
        WorkOrderFilter filter,
        int page,
        int size,
        String sortBy,
        String direction) {
}
