package com.mecanicadm.mecanicadm_api.core.vehicle.service;

import com.mecanicadm.mecanicadm_api.core.vehicle.domain.Vehicle;
import com.mecanicadm.mecanicadm_api.core.vehicle.domain.port.VehicleGateway;
import com.mecanicadm.mecanicadm_api.core.vehicle.exception.VehicleExceptions;
import com.mecanicadm.mecanicadm_api.core.vehicle.usecase.CreateVehicleUseCase;
import com.mecanicadm.mecanicadm_api.core.vehicle.usecase.command.CreateVehicleCommand;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementação do use case de criação de veículo.
 * Responsabilidades:
 * - Orquestrar o fluxo de criação
 * - Verificar regras de negócio
 * - Chamar métodos da entidade (Rich Domain Model)
 * - Persistir usando o gateway
 */
@Service
public class CreateVehicleService implements CreateVehicleUseCase {

    private final VehicleGateway vehicleGateway;

    public CreateVehicleService(VehicleGateway vehicleGateway) {
        this.vehicleGateway = vehicleGateway;
    }

    @Override
    @Transactional
    public String handle(CreateVehicleCommand cmd) {
        // Verifica se veículo já existe
        vehicleGateway.findByLicensePlate(cmd.licensePlate()).ifPresent(v -> {
            throw new VehicleExceptions.VehicleExists();
        });

        // Factory method da entidade garante estado válido
        // Todas as validações de negócio acontecem aqui
        Vehicle newVehicle = Vehicle.create(
                cmd.model(),
                cmd.licensePlate(),
                cmd.brand(),
                cmd.modelYear()
        );

        // Persiste no banco
        newVehicle = vehicleGateway.save(newVehicle);

        return newVehicle.getLicensePlate();
    }
}
