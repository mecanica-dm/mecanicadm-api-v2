package com.mecanicadm.mecanicadm_api.core.vehicle.usecase;

import com.mecanicadm.mecanicadm_api.core.vehicle.usecase.command.DeleteVehicleCommand;

public interface DeleteVehicleUseCase {
    void handle(DeleteVehicleCommand cmd);
}
