package com.mecanicadm.mecanicadm_api.core.vehicle.usecase;

import com.mecanicadm.mecanicadm_api.core.vehicle.domain.Vehicle;
import com.mecanicadm.mecanicadm_api.core.vehicle.domain.port.VehicleGateway;
import com.mecanicadm.mecanicadm_api.core.vehicle.exception.VehicleExceptions;
import com.mecanicadm.mecanicadm_api.core.vehicle.usecase.command.DeleteVehicleCommand;
import com.mecanicadm.mecanicadm_api.shared.usecase.VoidUseCase;

public class DeleteVehicleUseCase implements VoidUseCase<DeleteVehicleCommand> {

    private final VehicleGateway gateway;

    public DeleteVehicleUseCase(VehicleGateway gateway) {
        this.gateway = gateway;
    }

    public void execute(DeleteVehicleCommand command) {
        Vehicle vehicle = gateway.findByLicensePlate(command.licensePlate())
                .orElseThrow(VehicleExceptions.NotFound::new);
        vehicle.delete();
        gateway.update(vehicle);
    }
}

