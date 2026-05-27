package com.mecanicadm.mecanicadm_api.core.vehicle.usecase.command;

import com.mecanicadm.mecanicadm_api.infra.validation.annotation.LicensePlate;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateVehicleCommand(
        @NotBlank(message = "{validation.vehicle.model.not.blank}")
        String model,

        @NotBlank(message = "{validation.vehicle.licensePlate.not.blank}")
        @LicensePlate
        String licensePlate,

        @NotBlank(message = "{validation.vehicle.brand.not.blank}")
        String brand,

        @NotNull(message = "{validation.vehicle.modelYear.not.blank}")
        Short modelYear
) {
}