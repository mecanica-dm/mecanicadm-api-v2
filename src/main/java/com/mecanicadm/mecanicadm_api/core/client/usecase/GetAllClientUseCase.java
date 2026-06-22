package com.mecanicadm.mecanicadm_api.core.client.usecase;

import com.mecanicadm.mecanicadm_api.core.client.domain.port.ClientFilter;
import com.mecanicadm.mecanicadm_api.core.client.domain.port.ClientGateway;
import com.mecanicadm.mecanicadm_api.core.client.domain.port.ClientPageQuery;
import com.mecanicadm.mecanicadm_api.core.client.domain.port.ClientPageResult;
import com.mecanicadm.mecanicadm_api.core.client.usecase.query.GetAllClientQuery;
import com.mecanicadm.mecanicadm_api.shared.usecase.UseCase;

public class GetAllClientUseCase implements UseCase<GetAllClientQuery, ClientPageResult> {

    private final ClientGateway gateway;

    public GetAllClientUseCase(ClientGateway gateway) {
        this.gateway = gateway;
    }

    @Override
    public ClientPageResult execute(GetAllClientQuery query) {
        ClientFilter filter = new ClientFilter(query.name(), query.document());
        ClientPageQuery pageQuery = new ClientPageQuery(filter, query.page(), query.size(), query.sortBy(), query.direction());
        return gateway.findAll(pageQuery);
    }
}
