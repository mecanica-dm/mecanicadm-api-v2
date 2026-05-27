package com.mecanicadm.mecanicadm_api.core.vehicle.service;

import com.mecanicadm.mecanicadm_api.core.vehicle.adapter.repository.VehicleRepository;
import com.mecanicadm.mecanicadm_api.core.vehicle.domain.Vehicle;
import com.mecanicadm.mecanicadm_api.core.vehicle.exception.VehicleExceptions;
import com.mecanicadm.mecanicadm_api.core.vehicle.usecase.UpdateVehicleUseCase;
import com.mecanicadm.mecanicadm_api.core.vehicle.usecase.command.UpdateVehicleCommand;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UpdateVehicleService implements UpdateVehicleUseCase {

    private final VehicleRepository repository;

    public UpdateVehicleService(VehicleRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional
    public String handle(UpdateVehicleCommand cmd) {
        Vehicle vehicle = repository.findById(cmd.licensePlate())
                .orElseThrow(VehicleExceptions.NotFound::new);

        vehicle.update(cmd.model(), cmd.brand(), cmd.modelYear());
        return repository.save(vehicle).getLicensePlate();
    }
}
