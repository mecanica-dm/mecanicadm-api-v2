package com.mecanicadm.mecanicadm_api.core.workorders.usecase.dto;

public record PrintableBudgetResponse(
        String fileName,
        String base64Content
) {
}
