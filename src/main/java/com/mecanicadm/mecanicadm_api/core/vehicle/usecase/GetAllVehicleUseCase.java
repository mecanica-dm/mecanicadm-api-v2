package com.mecanicadm.mecanicadm_api.core.vehicle.usecase;

import com.mecanicadm.mecanicadm_api.core.vehicle.domain.port.VehicleFilter;
import com.mecanicadm.mecanicadm_api.core.vehicle.domain.port.VehicleGateway;
import com.mecanicadm.mecanicadm_api.core.vehicle.domain.port.VehiclePageQuery;
import com.mecanicadm.mecanicadm_api.core.vehicle.domain.port.VehiclePageResult;
import com.mecanicadm.mecanicadm_api.core.vehicle.usecase.query.GetAllVehiclesQuery;

public class GetAllVehicleUseCase {

    private final VehicleGateway gateway;

    public GetAllVehicleUseCase(VehicleGateway gateway) {
        this.gateway = gateway;
    }

    public VehiclePageResult execute(GetAllVehiclesQuery query) {
        VehicleFilter filter = new VehicleFilter(query.licensePlate(), query.model(), query.brand(), query.modelYear());
        VehiclePageQuery pageQuery = new VehiclePageQuery(filter, query.page(), query.size(), query.sortBy(), query.direction());
        return gateway.findAll(pageQuery);
    }
}

