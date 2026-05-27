package com.mecanicadm.mecanicadm_api.core.vehicle.usecase;

import com.mecanicadm.mecanicadm_api.core.vehicle.domain.Vehicle;
import com.mecanicadm.mecanicadm_api.core.vehicle.usecase.query.GetAllVehiclesQuery;
import org.springframework.data.domain.Page;

public interface GetAllVehicleUseCase {
    Page<Vehicle> handle(GetAllVehiclesQuery query);
}
