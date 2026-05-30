package com.mecanicadm.mecanicadm_api.core.vehicle.usecase.command;

public record CreateVehicleCommand(String model,
                                   String licensePlate,
                                   String brand,
                                   Short modelYear) {
}
