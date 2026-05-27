package com.mecanicadm.mecanicadm_api.core.vehicle.service;

import com.mecanicadm.mecanicadm_api.core.vehicle.adapter.repository.VehicleRepository;
import com.mecanicadm.mecanicadm_api.core.vehicle.exception.VehicleExceptions;
import com.mecanicadm.mecanicadm_api.core.vehicle.usecase.DeleteVehicleUseCase;
import com.mecanicadm.mecanicadm_api.core.vehicle.usecase.command.DeleteVehicleCommand;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DeleteVehicleService implements DeleteVehicleUseCase {

    private final VehicleRepository repository;

    public DeleteVehicleService(VehicleRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional
    public void handle(DeleteVehicleCommand cmd) {
        if (!repository.existsById(cmd.licensePlate())) {
            throw new VehicleExceptions.NotFound();
        }
        repository.deleteById(cmd.licensePlate());
    }
}
