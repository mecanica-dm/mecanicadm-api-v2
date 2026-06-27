package com.mecanicadm.mecanicadm_api.infra.features.user.persistence.jpa;

import com.mecanicadm.mecanicadm_api.core.user.domain.User;
import com.mecanicadm.mecanicadm_api.core.user.domain.port.UserGateway;
import com.mecanicadm.mecanicadm_api.core.user.domain.port.UserPageQuery;
import com.mecanicadm.mecanicadm_api.core.user.domain.port.UserPageResult;
import com.mecanicadm.mecanicadm_api.core.user.exception.UserExceptions;
import com.mecanicadm.mecanicadm_api.infra.features.user.persistence.entity.UserJpaEntity;
import com.mecanicadm.mecanicadm_api.infra.features.user.persistence.jpa.specification.UserSpecificationBuilder;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Repository
public class UserRepositoryImpl implements UserGateway {

    private final UserJpaRepository jpaRepository;

    public UserRepositoryImpl(UserJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public User create(User user) {
        UserJpaEntity saved = jpaRepository.save(UserJpaMapper.toEntity(user));
        return UserJpaMapper.toDomain(saved);
    }

    @Override
    @Transactional
    public User update(User user) {
        UserJpaEntity entity = jpaRepository.findById(user.getId())
                .orElseThrow(UserExceptions.NotFound::new);

        UserJpaMapper.updateEntity(entity, user);
        
        UserJpaEntity saved = jpaRepository.save(entity);
        return UserJpaMapper.toDomain(saved);
    }

    @Override
    public Optional<User> findById(UUID id) {
        return jpaRepository.findById(id).map(UserJpaMapper::toDomain);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return jpaRepository.findByEmail(email).map(UserJpaMapper::toDomain);
    }

    @Override
    public void deleteById(UUID id) {
        jpaRepository.deleteById(id);
    }

    @Override
    public void save(User user) {
        jpaRepository.save(UserJpaMapper.toEntity(user));
    }

    @Override
    public boolean existsById(UUID id) {
        return jpaRepository.existsById(id);
    }

    @Override
    public UserPageResult findAll(UserPageQuery query) {
        Specification<UserJpaEntity> spec = UserSpecificationBuilder.buildFilterSpecification(query.filter());
        Sort sort = Sort.by(Sort.Direction.fromString(query.direction()), query.sortBy());
        Pageable pageable = PageRequest.of(query.page(), query.size(), sort);
        var page = jpaRepository.findAll(spec, pageable);

        return new UserPageResult(
                page.map(UserJpaMapper::toDomain).getContent(),
                page.getTotalElements()
        );
    }
}
