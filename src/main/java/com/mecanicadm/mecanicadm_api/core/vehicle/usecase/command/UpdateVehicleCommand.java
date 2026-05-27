package com.mecanicadm.mecanicadm_api.core.vehicle.usecase.command;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mecanicadm.mecanicadm_api.infra.validation.annotation.LicensePlate;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UpdateVehicleCommand(
        @JsonIgnore
        @LicensePlate
        String licensePlate,

        @NotBlank(message = "{validation.vehicle.model.not.blank}")
        String model,


        @NotBlank(message = "{validation.vehicle.brand.not.blank}")
        String brand,

        @NotNull(message = "{validation.vehicle.modelYear.not.blank}")
        Short modelYear
) {
    public UpdateVehicleCommand withId(String licensePlate) {
        return new UpdateVehicleCommand(licensePlate, this.model, this.brand, this.modelYear);
    }
}