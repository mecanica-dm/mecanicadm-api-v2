package com.mecanicadm.mecanicadm_api.infra.features.vehicle.persistence.jpa;

import com.mecanicadm.mecanicadm_api.infra.features.vehicle.persistence.entity.VehicleJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface VehicleJpaRepository extends JpaRepository<VehicleJpaEntity, String>, JpaSpecificationExecutor<VehicleJpaEntity> {
    Optional<VehicleJpaEntity> findByLicensePlate(String licensePlate);
}

