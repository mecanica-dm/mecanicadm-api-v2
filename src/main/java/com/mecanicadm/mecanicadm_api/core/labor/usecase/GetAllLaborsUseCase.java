package com.mecanicadm.mecanicadm_api.core.labor.usecase;

import com.mecanicadm.mecanicadm_api.core.labor.adapter.api.dto.LaborResponse;
import com.mecanicadm.mecanicadm_api.core.labor.usecase.query.SearchLaborsQuery;
import org.springframework.data.domain.Page;

public interface GetAllLaborsUseCase {
    Page<LaborResponse> handle(SearchLaborsQuery query);
}
