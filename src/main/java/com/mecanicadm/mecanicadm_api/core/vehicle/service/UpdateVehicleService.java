package com.mecanicadm.mecanicadm_api.core.vehicle.service;

import com.mecanicadm.mecanicadm_api.core.vehicle.domain.Vehicle;
import com.mecanicadm.mecanicadm_api.core.vehicle.domain.port.VehicleGateway;
import com.mecanicadm.mecanicadm_api.core.vehicle.exception.VehicleExceptions;
import com.mecanicadm.mecanicadm_api.core.vehicle.usecase.UpdateVehicleUseCase;
import com.mecanicadm.mecanicadm_api.core.vehicle.usecase.command.UpdateVehicleCommand;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementação do use case de atualização de veículo.
 */
@Service
public class UpdateVehicleService implements UpdateVehicleUseCase {

    private final VehicleGateway vehicleGateway;

    public UpdateVehicleService(VehicleGateway vehicleGateway) {
        this.vehicleGateway = vehicleGateway;
    }

    @Override
    @Transactional
    public String handle(UpdateVehicleCommand cmd) {
        Vehicle vehicle = vehicleGateway.findById(cmd.licensePlate())
                .orElseThrow(VehicleExceptions.NotFound::new);

        // Usa o método da entidade que encapsula as validações
        vehicle.updateInfo(cmd.model(), cmd.brand(), cmd.modelYear());
        return vehicleGateway.save(vehicle).getLicensePlate();
    }
}
