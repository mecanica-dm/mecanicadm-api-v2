package com.mecanicadm.mecanicadm_api.core.client.usecase;

import com.mecanicadm.mecanicadm_api.core.client.adapter.api.dto.ClientResponse;
import com.mecanicadm.mecanicadm_api.core.client.usecase.query.GetAllClientQuery;
import org.springframework.data.domain.Page;

public interface GetAllClientUseCase {
    Page<ClientResponse> handle(GetAllClientQuery query);
}
