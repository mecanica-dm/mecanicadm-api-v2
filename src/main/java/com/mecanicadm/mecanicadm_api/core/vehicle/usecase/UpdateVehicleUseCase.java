package com.mecanicadm.mecanicadm_api.core.vehicle.usecase;

import com.mecanicadm.mecanicadm_api.core.vehicle.domain.Vehicle;
import com.mecanicadm.mecanicadm_api.core.vehicle.domain.port.VehicleGateway;
import com.mecanicadm.mecanicadm_api.core.vehicle.exception.VehicleExceptions;
import com.mecanicadm.mecanicadm_api.core.vehicle.usecase.command.UpdateVehicleCommand;

public class UpdateVehicleUseCase {

    private final VehicleGateway gateway;

    public UpdateVehicleUseCase(VehicleGateway gateway) {
        this.gateway = gateway;
    }

    public Vehicle execute(UpdateVehicleCommand command) {
        Vehicle vehicle = gateway.findByLicensePlate(command.licensePlate())
                .orElseThrow(VehicleExceptions.NotFound::new);
        Vehicle updated = vehicle.update(command.model(), command.brand(), command.modelYear());
        return gateway.update(updated);
    }
}

