package com.mecanicadm.mecanicadm_api.core.material.usecase;

import com.mecanicadm.mecanicadm_api.core.material.domain.port.MaterialFilter;
import com.mecanicadm.mecanicadm_api.core.material.domain.port.MaterialGateway;
import com.mecanicadm.mecanicadm_api.core.material.domain.port.MaterialPageQuery;
import com.mecanicadm.mecanicadm_api.core.material.domain.port.MaterialPageResult;
import com.mecanicadm.mecanicadm_api.core.material.usecase.query.SearchMaterialsQuery;

public class GetAllMaterialsUseCase {

    private final MaterialGateway gateway;

    public GetAllMaterialsUseCase(MaterialGateway gateway) {
        this.gateway = gateway;
    }

    public MaterialPageResult execute(SearchMaterialsQuery query) {
        MaterialFilter filter = new MaterialFilter(query.name(), query.brand(), query.type());
        MaterialPageQuery pageQuery = new MaterialPageQuery(filter, query.page(), query.size(), query.sortBy(), query.direction());
        return gateway.findAll(pageQuery);
    }
}
