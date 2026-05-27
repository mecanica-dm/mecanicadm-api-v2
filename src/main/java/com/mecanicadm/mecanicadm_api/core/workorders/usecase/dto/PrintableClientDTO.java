package com.mecanicadm.mecanicadm_api.core.workorders.usecase.dto;

public record PrintableClientDTO(
        String name,
        String document,
        String phone,
        String email
) {}
