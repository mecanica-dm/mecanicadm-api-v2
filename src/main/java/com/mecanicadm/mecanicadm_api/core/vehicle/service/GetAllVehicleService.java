package com.mecanicadm.mecanicadm_api.core.vehicle.service;

import com.mecanicadm.mecanicadm_api.core.vehicle.adapter.repository.VehicleRepository;
import com.mecanicadm.mecanicadm_api.core.vehicle.domain.Vehicle;
import com.mecanicadm.mecanicadm_api.core.vehicle.usecase.GetAllVehicleUseCase;
import com.mecanicadm.mecanicadm_api.core.vehicle.usecase.query.GetAllVehiclesQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static io.micrometer.common.util.StringUtils.isNotBlank;
import static java.util.Objects.nonNull;

/**
 * Implementação do use case de busca de todos os veículos com filtros.
 * VehicleRepository implementa VehicleGateway, então podemos fazer cast se necessário.
 */
@Service
public class GetAllVehicleService implements GetAllVehicleUseCase {

    private final VehicleRepository repository;

    public GetAllVehicleService(VehicleRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional
    public Page<Vehicle> handle(GetAllVehiclesQuery query) {
        Specification<Vehicle> spec = Specification.where(null);

        if (isNotBlank(query.licensePlate())) {
            spec = spec.and((root, q, cb) ->
                    cb.like(root.get("licensePlate"), "%" + query.licensePlate() + "%"));
        }

        if (isNotBlank(query.model())) {
            spec = spec.and((root, q, cb) ->
                    cb.like(root.get("model"), "%" + query.model() + "%"));
        }

        if (isNotBlank(query.brand())) {
            spec = spec.and((root, q, cb) ->
                    cb.equal(root.get("brand"), query.brand()));
        }

        if (nonNull(query.modelYear())) {
            spec = spec.and((root, q, cb) ->
                    cb.equal(root.get("modelYear"), query.modelYear()));
        }

        return repository.findAll(spec, query.pageable());
    }
}
