package com.mecanicadm.mecanicadm_api.core.vehicle.usecase;

import com.mecanicadm.mecanicadm_api.core.vehicle.domain.port.VehicleGateway;
import com.mecanicadm.mecanicadm_api.core.vehicle.exception.VehicleExceptions;
import com.mecanicadm.mecanicadm_api.core.vehicle.usecase.command.DeleteVehicleCommand;

public class DeleteVehicleUseCase {

    private final VehicleGateway gateway;

    public DeleteVehicleUseCase(VehicleGateway gateway) {
        this.gateway = gateway;
    }

    public void execute(DeleteVehicleCommand command) {
        if (!gateway.existsByLicensePlate(command.licensePlate())) {
            throw new VehicleExceptions.NotFound();
        }
        gateway.deleteByLicensePlate(command.licensePlate());
    }
}

