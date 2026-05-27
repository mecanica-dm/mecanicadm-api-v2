package com.mecanicadm.mecanicadm_api.core.material.usecase.query;

import com.mecanicadm.mecanicadm_api.core.material.domain.enums.MaterialType;
import jakarta.annotation.Nullable;
import org.springframework.data.domain.Pageable;

public record SearchMaterialsQuery(
        @Nullable String name,
        @Nullable String brand,
        @Nullable MaterialType type,
        Pageable pageable
) {
}
