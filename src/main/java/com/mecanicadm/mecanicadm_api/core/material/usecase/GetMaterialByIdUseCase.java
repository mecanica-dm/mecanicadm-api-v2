package com.mecanicadm.mecanicadm_api.core.material.usecase;

import com.mecanicadm.mecanicadm_api.core.material.adapter.api.dto.MaterialResponse;
import com.mecanicadm.mecanicadm_api.core.material.usecase.query.GetMaterialByIdQuery;

public interface GetMaterialByIdUseCase {
    MaterialResponse handle(GetMaterialByIdQuery query);
}
