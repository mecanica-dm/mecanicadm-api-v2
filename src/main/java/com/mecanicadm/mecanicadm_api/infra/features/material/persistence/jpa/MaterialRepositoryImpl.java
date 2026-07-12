package com.mecanicadm.mecanicadm_api.infra.features.material.persistence.jpa;

import com.mecanicadm.mecanicadm_api.core.material.domain.Material;
import com.mecanicadm.mecanicadm_api.core.material.domain.port.MaterialGateway;
import com.mecanicadm.mecanicadm_api.core.material.domain.port.MaterialPageQuery;
import com.mecanicadm.mecanicadm_api.core.material.domain.port.MaterialPageResult;
import com.mecanicadm.mecanicadm_api.infra.features.material.persistence.entity.MaterialJpaEntity;
import com.mecanicadm.mecanicadm_api.infra.features.material.persistence.jpa.specification.MaterialSpecificationBuilder;
import com.mecanicadm.mecanicadm_api.shared.exception.TechnicalException;
import com.mecanicadm.mecanicadm_api.shared.validation.SortValidator;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static java.util.Objects.isNull;

@Repository
public class MaterialRepositoryImpl implements MaterialGateway {

    private static final Set<String> ALLOWED_SORT_FIELDS = Set.of("name", "brand", "price", "type", "quantity", "dateCreated");

    private final MaterialJpaRepository jpaRepository;

    public MaterialRepositoryImpl(MaterialJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Material create(Material material) {
        if (isNull(material)) {
            throw new TechnicalException("error.technical.entity.null", "Material", "criação");
        }
        MaterialJpaEntity saved = jpaRepository.save(MaterialJpaMapper.toEntity(material));
        return MaterialJpaMapper.toDomain(saved);
    }

    @Override
    public Material update(Material material) {
        if (isNull(material)) {
            throw new TechnicalException("error.technical.entity.null", "Material", "atualização");
        }
        MaterialJpaEntity entity = MaterialJpaMapper.toEntity(material);
        MaterialJpaEntity saved = jpaRepository.save(entity);
        return MaterialJpaMapper.toDomain(saved);
    }

    @Override
    public Optional<Material> findById(UUID id) {
        return jpaRepository.findActiveById(id).map(MaterialJpaMapper::toDomain);
    }

    @Override
    public boolean existsById(UUID id) {
        return jpaRepository.existsById(id);
    }

    @Override
    public MaterialPageResult findAll(MaterialPageQuery query) {
        Specification<MaterialJpaEntity> spec = MaterialSpecificationBuilder.buildFilterSpecification(query.filter());
        Sort sort = SortValidator.safeSort(query.sortBy(), query.direction(), ALLOWED_SORT_FIELDS, "name");
        Pageable pageable = PageRequest.of(query.page(), query.size(), sort);
        var page = jpaRepository.findAll(spec, pageable);

        return new MaterialPageResult(
                page.map(MaterialJpaMapper::toDomain).getContent(),
                page.getTotalElements()
        );
    }

    @Override
    public List<Material> findAllByIds(Set<UUID> ids) {
        return jpaRepository.findAllByIds(ids).stream().map(MaterialJpaMapper::toDomain).toList();
    }
}
