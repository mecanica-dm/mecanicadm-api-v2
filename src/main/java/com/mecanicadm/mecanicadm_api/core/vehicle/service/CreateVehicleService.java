package com.mecanicadm.mecanicadm_api.core.vehicle.service;

import com.mecanicadm.mecanicadm_api.core.vehicle.adapter.repository.VehicleRepository;
import com.mecanicadm.mecanicadm_api.core.vehicle.domain.Vehicle;
import com.mecanicadm.mecanicadm_api.core.vehicle.exception.VehicleExceptions;
import com.mecanicadm.mecanicadm_api.core.vehicle.usecase.CreateVehicleUseCase;
import com.mecanicadm.mecanicadm_api.core.vehicle.usecase.command.CreateVehicleCommand;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CreateVehicleService implements CreateVehicleUseCase {

    private final VehicleRepository repository;

    public CreateVehicleService(VehicleRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional
    public String handle(CreateVehicleCommand cmd) {
        repository.findByLicensePlate(cmd.licensePlate()).ifPresent(v -> {
            throw new VehicleExceptions.VehicleExists();
        });

        Vehicle newVehicle = new Vehicle(
                cmd.model(),
                cmd.licensePlate(),
                cmd.brand(),
                cmd.modelYear()
        );

        newVehicle = repository.save(newVehicle);

        return newVehicle.getLicensePlate();
    }
}
