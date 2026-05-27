package com.mecanicadm.mecanicadm_api.core.vehicle.domain;

import com.mecanicadm.mecanicadm_api.infra.baseentities.AuditEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Table(name = "vehicle")
@SQLDelete(sql = "UPDATE vehicle SET deleted_at = now() WHERE id = ?")
@SQLRestriction("deleted_at IS NULL")
public class Vehicle extends AuditEntity {
    @Id
    private String licensePlate;
    private String model;
    private String brand;
    private Short modelYear;

    public Vehicle() {
    }

    public Vehicle(String model, String licensePlate, String brand, Short modelYear) {
        this.model = model;
        this.licensePlate = licensePlate;
        this.brand = brand;
        this.modelYear = modelYear;
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

    public void update(String model, String brand, Short modelYear) {
        this.model = model;
        this.brand = brand;
        this.modelYear = modelYear;
    }
}
