package com.mecanicadm.mecanicadm_api.core.vehicle.usecase.query;

import org.springframework.data.domain.Pageable;

public record GetAllVehiclesQuery(String licensePlate, String model, String brand, Short modelYear, Pageable pageable) {
}