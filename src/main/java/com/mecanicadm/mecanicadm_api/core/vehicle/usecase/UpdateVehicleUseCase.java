package com.mecanicadm.mecanicadm_api.core.vehicle.usecase;

import com.mecanicadm.mecanicadm_api.core.vehicle.usecase.command.UpdateVehicleCommand;

public interface UpdateVehicleUseCase {
    String handle(UpdateVehicleCommand cmd);
}
