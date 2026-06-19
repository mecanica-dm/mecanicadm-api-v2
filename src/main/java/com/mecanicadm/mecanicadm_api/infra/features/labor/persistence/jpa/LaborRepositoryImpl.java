package com.mecanicadm.mecanicadm_api.infra.features.labor.persistence.jpa;

import com.mecanicadm.mecanicadm_api.core.labor.domain.Labor;
import com.mecanicadm.mecanicadm_api.core.labor.domain.port.LaborGateway;
import com.mecanicadm.mecanicadm_api.core.labor.domain.port.LaborPageQuery;
import com.mecanicadm.mecanicadm_api.core.labor.domain.port.LaborPageResult;
import com.mecanicadm.mecanicadm_api.infra.features.labor.persistence.entity.LaborJpaEntity;
import com.mecanicadm.mecanicadm_api.infra.features.labor.persistence.jpa.specification.LaborSpecificationBuilder;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Repository
public class LaborRepositoryImpl implements LaborGateway {

    private final LaborJpaRepository jpaRepository;

    public LaborRepositoryImpl(LaborJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Labor create(Labor labor) {
        LaborJpaEntity saved = jpaRepository.save(LaborJpaMapper.toEntity(labor));
        return LaborJpaMapper.toDomain(saved);
    }

    @Override
    public Labor update(Labor labor) {
        LaborJpaEntity saved = jpaRepository.save(LaborJpaMapper.toEntity(labor));
        return LaborJpaMapper.toDomain(saved);
    }

    @Override
    public void deleteById(UUID id) {
        jpaRepository.deleteById(id);
    }

    @Override
    public Optional<Labor> findById(UUID id) {
        return jpaRepository.findById(id).map(LaborJpaMapper::toDomain);
    }

    @Override
    public LaborPageResult findAll(LaborPageQuery query) {
        Specification<LaborJpaEntity> spec = LaborSpecificationBuilder.buildFilterSpecification(query.filter());
        Sort sort = Sort.by(Sort.Direction.fromString(query.direction()), query.sortBy());
        Pageable pageable = PageRequest.of(query.page(), query.size(), sort);
        var page = jpaRepository.findAll(spec, pageable);

        return new LaborPageResult(
                page.map(LaborJpaMapper::toDomain).getContent(),
                page.getTotalElements()
        );
    }

    @Override
    public boolean existsById(UUID id) {
        return jpaRepository.existsById(id);
    }

    @Override
    public List<Labor> findAllByIds(Set<UUID> ids) {
        return jpaRepository.findAllByIds(ids).stream().map(LaborJpaMapper::toDomain).toList();
    }
}

