package com.mecanicadm.mecanicadm_api.core.labor.usecase.query;


import org.springframework.data.domain.Pageable;

public record SearchLaborsQuery(String name, Pageable pageable) {
}
