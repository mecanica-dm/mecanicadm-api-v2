package com.mecanicadm.mecanicadm_api.core.vehicle.domain.port;

import com.mecanicadm.mecanicadm_api.core.vehicle.domain.Vehicle;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Porta de saída (Gateway) para persistência de Vehicle.
 * Define o contrato que implementações de repository devem seguir.
 * Isso garante inversão de dependência: o domínio não depende do framework.
 */
public interface VehicleGateway {
    Vehicle save(Vehicle vehicle);
    Optional<Vehicle> findById(String licensePlate);
    Optional<Vehicle> findByLicensePlate(String licensePlate);
    Page<Vehicle> findAll(Pageable pageable);
    void delete(Vehicle vehicle);
}

