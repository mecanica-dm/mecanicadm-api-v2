package com.mecanicadm.mecanicadm_api.core.vehicle.usecase.command;

import jakarta.validation.constraints.NotBlank;

public record DeleteVehicleCommand(
        @NotBlank(message = "{validation.vehicle.licensePlate.notBlank}") String licensePlate) {
}
