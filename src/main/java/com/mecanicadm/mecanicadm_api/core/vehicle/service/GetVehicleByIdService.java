package com.mecanicadm.mecanicadm_api.core.vehicle.service;

import com.mecanicadm.mecanicadm_api.core.vehicle.adapter.repository.VehicleRepository;
import com.mecanicadm.mecanicadm_api.core.vehicle.domain.Vehicle;
import com.mecanicadm.mecanicadm_api.core.vehicle.exception.VehicleExceptions;
import com.mecanicadm.mecanicadm_api.core.vehicle.usecase.GetVehicleByIdUseCase;
import com.mecanicadm.mecanicadm_api.core.vehicle.usecase.query.GetVehicleByIdQuery;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class GetVehicleByIdService implements GetVehicleByIdUseCase {

    private final VehicleRepository repository;

    public GetVehicleByIdService(VehicleRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional
    public Vehicle handle(GetVehicleByIdQuery query) {
        return repository.findById(query.licensePlate()).orElseThrow(VehicleExceptions.NotFound::new);
    }
}
