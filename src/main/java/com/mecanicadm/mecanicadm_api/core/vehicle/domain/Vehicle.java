package com.mecanicadm.mecanicadm_api.core.vehicle.domain;

import com.mecanicadm.mecanicadm_api.core.vehicle.exception.VehicleExceptions;
import com.mecanicadm.mecanicadm_api.infra.baseentities.AuditEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.springframework.util.StringUtils;

import java.time.Year;

@Entity
@Table(name = "vehicle")
@SQLDelete(sql = "UPDATE vehicle SET deleted_at = now() WHERE license_plate = ?")
@SQLRestriction("deleted_at IS NULL")
public class Vehicle extends AuditEntity {
    @Id
    private String licensePlate;
    private String model;
    private String brand;
    private Short modelYear;

    protected Vehicle() {
    }

    private Vehicle(String model, String licensePlate, String brand, Short modelYear) {
        this.model = model;
        this.licensePlate = licensePlate;
        this.brand = brand;
        this.modelYear = modelYear;
        validate();
    }

    /**
     * Factory method para criar um Vehicle validado.
     * Garante que a entidade sempre estará em um estado válido.
     */
    public static Vehicle create(String model, String licensePlate, String brand, Short modelYear) {
        return new Vehicle(model, licensePlate, brand, modelYear);
    }

    /**
     * Método de negócio para atualizar informações do veículo.
     * Garante consistência das validações.
     */
    public void updateInfo(String model, String brand, Short modelYear) {
        this.model = model;
        this.brand = brand;
        this.modelYear = modelYear;
        validate();
    }

    /**
     * Orquestra todas as validações de negócio da entidade (Rich Domain Model).
     * Todas as regras de validação ficam aqui, não no service.
     */
    private void validate() {
        validateLicensePlate();
        validateModel();
        validateBrand();
        validateModelYear();
    }

    private void validateLicensePlate() {
        if (!StringUtils.hasText(this.licensePlate)) {
            throw new VehicleExceptions.LicensePlateNotEmpty();
        }
        if (this.licensePlate.length() < 6) {
            throw new VehicleExceptions.InvalidLicensePlate();
        }
    }

    private void validateModel() {
        if (!StringUtils.hasText(this.model)) {
            throw new VehicleExceptions.ModelNotEmpty();
        }
    }

    private void validateBrand() {
        if (!StringUtils.hasText(this.brand)) {
            throw new VehicleExceptions.BrandNotEmpty();
        }
    }

    private void validateModelYear() {
        if (this.modelYear == null || this.modelYear <= 0) {
            throw new VehicleExceptions.InvalidModelYear();
        }
        if (this.modelYear > Year.now().getValue()) {
            throw new VehicleExceptions.ModelYearCannotBeInFuture();
        }
    }

    // Getters
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
