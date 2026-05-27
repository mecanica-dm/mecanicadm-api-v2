package com.mecanicadm.mecanicadm_api.core.vehicle.adapter.api.dto;

import com.mecanicadm.mecanicadm_api.core.vehicle.domain.Vehicle;

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
