package com.mecanicadm.mecanicadm_api.infra.features.vehicle.persistence.jpa;

import com.mecanicadm.mecanicadm_api.core.vehicle.domain.Vehicle;
import com.mecanicadm.mecanicadm_api.core.vehicle.domain.port.VehicleGateway;
import com.mecanicadm.mecanicadm_api.core.vehicle.domain.port.VehiclePageQuery;
import com.mecanicadm.mecanicadm_api.core.vehicle.domain.port.VehiclePageResult;
import com.mecanicadm.mecanicadm_api.infra.features.vehicle.persistence.entity.VehicleJpaEntity;
import com.mecanicadm.mecanicadm_api.infra.features.vehicle.persistence.jpa.specification.VehicleSpecificationBuilder;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class VehicleRepositoryImpl implements VehicleGateway {

    private final VehicleJpaRepository jpaRepository;

    public VehicleRepositoryImpl(VehicleJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Vehicle create(Vehicle vehicle) {
        VehicleJpaEntity entity = VehicleJpaMapper.toEntity(vehicle);
        VehicleJpaEntity saved = jpaRepository.save(entity);
        return VehicleJpaMapper.toDomain(saved);
    }

    @Override
    public Vehicle update(Vehicle vehicle) {
        VehicleJpaEntity entity = VehicleJpaMapper.toEntity(vehicle);
        VehicleJpaEntity saved = jpaRepository.save(entity);
        return VehicleJpaMapper.toDomain(saved);
    }

    @Override
    public void deleteByLicensePlate(String licensePlate) {
        jpaRepository.deleteById(licensePlate);
    }

    @Override
    public Optional<Vehicle> findByLicensePlate(String licensePlate) {
        return jpaRepository.findByLicensePlate(licensePlate).map(VehicleJpaMapper::toDomain);
    }

    @Override
    public boolean existsByLicensePlate(String licensePlate) {
        return jpaRepository.existsById(licensePlate);
    }

    @Override
    public VehiclePageResult findAll(VehiclePageQuery query) {
        Specification<VehicleJpaEntity> spec = VehicleSpecificationBuilder.buildFilterSpecification(query.filter());
        Sort sort = Sort.by(Sort.Direction.fromString(query.direction()), query.sortBy());
        Pageable pageable = PageRequest.of(query.page(), query.size(), sort);

        var page = jpaRepository.findAll(spec, pageable);
        return new VehiclePageResult(
                page.map(VehicleJpaMapper::toDomain).getContent(),
                page.getTotalElements()
        );
    }
}
