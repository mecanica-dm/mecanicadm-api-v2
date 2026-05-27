package com.mecanicadm.mecanicadm_api.core.vehicle.service;

import com.mecanicadm.mecanicadm_api.core.vehicle.domain.Vehicle;
import com.mecanicadm.mecanicadm_api.core.vehicle.domain.port.VehicleGateway;
import com.mecanicadm.mecanicadm_api.core.vehicle.exception.VehicleExceptions;
import com.mecanicadm.mecanicadm_api.core.vehicle.usecase.DeleteVehicleUseCase;
import com.mecanicadm.mecanicadm_api.core.vehicle.usecase.command.DeleteVehicleCommand;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementação do use case de exclusão de veículo.
 * Utiliza soft delete através da anotação @SQLDelete na entidade.
 */
@Service
public class DeleteVehicleService implements DeleteVehicleUseCase {

    private final VehicleGateway vehicleGateway;

    public DeleteVehicleService(VehicleGateway vehicleGateway) {
        this.vehicleGateway = vehicleGateway;
    }

    @Override
    @Transactional
    public void handle(DeleteVehicleCommand cmd) {
        Vehicle vehicle = vehicleGateway.findById(cmd.licensePlate())
                .orElseThrow(VehicleExceptions.NotFound::new);
        vehicleGateway.delete(vehicle);
    }
}
