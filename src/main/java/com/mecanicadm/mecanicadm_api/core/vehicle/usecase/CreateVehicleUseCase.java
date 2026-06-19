package com.mecanicadm.mecanicadm_api.core.vehicle.usecase;

import com.mecanicadm.mecanicadm_api.core.vehicle.domain.Vehicle;
import com.mecanicadm.mecanicadm_api.core.vehicle.domain.port.VehicleGateway;
import com.mecanicadm.mecanicadm_api.core.vehicle.exception.VehicleExceptions;
import com.mecanicadm.mecanicadm_api.core.vehicle.usecase.command.CreateVehicleCommand;

public class CreateVehicleUseCase {

    private final VehicleGateway gateway;

    public CreateVehicleUseCase(VehicleGateway gateway) {
        this.gateway = gateway;
    }

    public String execute(CreateVehicleCommand command) {
        gateway.findByLicensePlate(command.licensePlate())
                .ifPresent(v -> {
                    throw new VehicleExceptions.VehicleExists(command.licensePlate());
                });
        Vehicle vehicle = Vehicle.create(command.model(), command.licensePlate(), command.brand(), command.modelYear());
        Vehicle created = gateway.create(vehicle);
        return created.getLicensePlate();
    }
}

