package com.mecanicadm.mecanicadm_api.core.labor.usecase.query;

public record SearchLaborsQuery(String name, int page, int size, String sortBy, String direction) {
}
