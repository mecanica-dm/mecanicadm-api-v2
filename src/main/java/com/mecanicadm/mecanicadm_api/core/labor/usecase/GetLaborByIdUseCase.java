package com.mecanicadm.mecanicadm_api.core.labor.usecase;

import com.mecanicadm.mecanicadm_api.core.labor.adapter.api.dto.LaborResponse;
import com.mecanicadm.mecanicadm_api.core.labor.usecase.query.GetLaborByIdQuery;

public interface GetLaborByIdUseCase {
    LaborResponse handle(GetLaborByIdQuery query);
}
