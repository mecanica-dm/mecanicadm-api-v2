package com.mecanicadm.mecanicadm_api.core.vehicle.adapter.api.dto;

import com.mecanicadm.mecanicadm_api.core.vehicle.domain.Vehicle;

/**
 * DTO de resposta para Vehicle.
 * Separa a representação da entidade de domínio da resposta HTTP.
 */
public class VehicleResponse {

    private final String licensePlate;
    private final String model;
    private final String brand;
    private final Short modelYear;

    public VehicleResponse(Vehicle vehicle) {
        this.licensePlate = vehicle.getLicensePlate();
        this.model = vehicle.getModel();
        this.brand = vehicle.getBrand();
        this.modelYear = vehicle.getModelYear();
    }

    public static VehicleResponse fromEntity(Vehicle vehicle) {
        return new VehicleResponse(vehicle);
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public String getModel() {
        return model;
    }

    public String getBrand() {
        return brand;
    }

    public Short getModelYear() {
        return modelYear;
    }
}
