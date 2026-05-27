package com.mecanicadm.mecanicadm_api.core.vehicle.usecase;

import com.mecanicadm.mecanicadm_api.core.vehicle.usecase.command.CreateVehicleCommand;

public interface CreateVehicleUseCase {
    String handle(CreateVehicleCommand cmd);
}
