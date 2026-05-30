package com.mecanicadm.mecanicadm_api.infra.features.vehicle.persistence.jpa;

import com.mecanicadm.mecanicadm_api.core.vehicle.domain.Vehicle;
import com.mecanicadm.mecanicadm_api.infra.features.vehicle.persistence.entity.VehicleJpaEntity;

import static java.util.Objects.isNull;

public class VehicleJpaMapper {

    public static Vehicle toDomain(VehicleJpaEntity entity) {
        if (isNull(entity)) return null;
        return new Vehicle(entity.getModel(),
                entity.getLicensePlate(),
                entity.getBrand(),
                entity.getModelYear()
        );
    }

    public static VehicleJpaEntity toEntity(Vehicle domain) {
        if (isNull(domain)) return null;
        return new VehicleJpaEntity(
                domain.getLicensePlate(),
                domain.getModel(),
                domain.getBrand(),
                domain.getModelYear()
        );
    }
}

