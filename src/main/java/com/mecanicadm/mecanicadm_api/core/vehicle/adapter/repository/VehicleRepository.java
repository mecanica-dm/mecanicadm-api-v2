package com.mecanicadm.mecanicadm_api.core.vehicle.adapter.repository;

import com.mecanicadm.mecanicadm_api.core.vehicle.domain.Vehicle;
import com.mecanicadm.mecanicadm_api.core.vehicle.domain.port.VehicleGateway;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Implementação de VehicleGateway usando Spring Data JPA.
 * Atua como um adaptador que implementa a interface de porta do domínio.
 * Isso garante inversão de dependência: o core depende da porta, não do framework.
 */
@Repository
public interface VehicleRepository extends VehicleGateway, JpaRepository<Vehicle, String>, JpaSpecificationExecutor<Vehicle> {
    Optional<Vehicle> findByLicensePlate(String licensePlate);
}
