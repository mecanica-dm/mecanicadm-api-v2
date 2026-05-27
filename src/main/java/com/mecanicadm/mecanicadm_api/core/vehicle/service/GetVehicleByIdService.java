package com.mecanicadm.mecanicadm_api.core.vehicle.service;

import com.mecanicadm.mecanicadm_api.core.vehicle.domain.Vehicle;
import com.mecanicadm.mecanicadm_api.core.vehicle.domain.port.VehicleGateway;
import com.mecanicadm.mecanicadm_api.core.vehicle.exception.VehicleExceptions;
import com.mecanicadm.mecanicadm_api.core.vehicle.usecase.GetVehicleByIdUseCase;
import com.mecanicadm.mecanicadm_api.core.vehicle.usecase.query.GetVehicleByIdQuery;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

/**
 * Implementação do use case de busca de veículo por ID.
 */
@Service
public class GetVehicleByIdService implements GetVehicleByIdUseCase {

    private final VehicleGateway vehicleGateway;

    public GetVehicleByIdService(VehicleGateway vehicleGateway) {
        this.vehicleGateway = vehicleGateway;
    }

    @Override
    @Transactional
    public Vehicle handle(GetVehicleByIdQuery query) {
        return vehicleGateway.findById(query.licensePlate()).orElseThrow(VehicleExceptions.NotFound::new);
    }
}
