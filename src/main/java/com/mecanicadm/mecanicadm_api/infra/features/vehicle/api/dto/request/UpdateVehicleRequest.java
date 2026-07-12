package com.mecanicadm.mecanicadm_api.infra.features.vehicle.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UpdateVehicleRequest(
        @NotBlank(message = "{validation.vehicle.model.not.blank}")
        @Size(max = 255, message = "{validation.vehicle.model.size}")
        String model,

        @NotBlank(message = "{validation.vehicle.brand.not.blank}")
        @Size(max = 255, message = "{validation.vehicle.brand.size}")
        String brand,

        @NotNull(message = "{validation.vehicle.modelYear.not.null}")
        Short modelYear
) {
}

