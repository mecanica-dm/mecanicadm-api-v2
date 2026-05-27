package com.mecanicadm.mecanicadm_api.core.material.usecase;

import com.mecanicadm.mecanicadm_api.core.material.adapter.api.dto.MaterialResponse;
import com.mecanicadm.mecanicadm_api.core.material.usecase.query.SearchMaterialsQuery;
import org.springframework.data.domain.Page;

public interface GetAllMaterialsUseCase {
    Page<MaterialResponse> handle(SearchMaterialsQuery query);
}
