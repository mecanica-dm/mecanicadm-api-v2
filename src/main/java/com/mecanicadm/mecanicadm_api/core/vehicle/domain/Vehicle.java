package com.mecanicadm.mecanicadm_api.core.vehicle.domain;

import com.mecanicadm.mecanicadm_api.core.vehicle.exception.VehicleExceptions;

import static java.util.Objects.isNull;

public class Vehicle {
    private final String licensePlate;
    private final String model;
    private final String brand;
    private final Short modelYear;

    public Vehicle(String model, String licensePlate, String brand, Short modelYear) {
        this.model = model;
        this.licensePlate = licensePlate;
        this.brand = brand;
        this.modelYear = modelYear;
        validate();
    }

    public static Vehicle create(String model, String licensePlate, String brand, Short modelYear) {
        return new Vehicle(model, licensePlate, brand, modelYear);
    }

    public Vehicle updateInfo(String model, String brand, Short modelYear) {
        return new Vehicle(model, this.getLicensePlate(), brand, modelYear);
    }

    public Vehicle update(String model, String brand, Short modelYear) {
        return updateInfo(model, brand, modelYear);
    }

    private void validate() {
        validateModel();
        validateBrand();
        validateModelYear();
    }

    private void validateModel() {
        if (isBlank(this.model)) {
            throw new VehicleExceptions.ModelNotEmpty();
        }
    }

    private void validateBrand() {
        if (isBlank(this.brand)) {
            throw new VehicleExceptions.BrandNotEmpty();
        }
    }

    private void validateModelYear() {
        if (isNull(this.modelYear)) {
            throw new VehicleExceptions.InvalidModelYear();
        }
        if (this.modelYear < 1886 || this.modelYear > 9999) {
            throw new VehicleExceptions.InvalidModelYear();
        }
    }

    private boolean isBlank(String value) {
        return isNull(value) || value.trim().isEmpty();
    }

    public String getLicensePlate() { return licensePlate; }
    public String getModel() { return model; }
    public String getBrand() { return brand; }
    public Short getModelYear() { return modelYear; }
}

