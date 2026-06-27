package com.mecanicadm.mecanicadm_api.infra.features.client.persistence.jpa;

import com.mecanicadm.mecanicadm_api.core.client.domain.Client;
import com.mecanicadm.mecanicadm_api.core.client.domain.port.ClientGateway;
import com.mecanicadm.mecanicadm_api.core.client.domain.port.ClientPageQuery;
import com.mecanicadm.mecanicadm_api.core.client.domain.port.ClientPageResult;
import com.mecanicadm.mecanicadm_api.infra.features.client.persistence.entity.ClientJpaEntity;
import com.mecanicadm.mecanicadm_api.infra.features.client.persistence.jpa.specification.ClientSpecificationBuilder;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class ClientRepositoryImpl implements ClientGateway {

    private final ClientJpaRepository jpaRepository;

    public ClientRepositoryImpl(ClientJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Client create(Client client) {
        ClientJpaEntity entity = ClientJpaMapper.toEntity(client);
        ClientJpaEntity saved = jpaRepository.save(entity);
        return ClientJpaMapper.toDomain(saved);
    }

    @Override
    public Client update(Client client) {
        ClientJpaEntity entity = ClientJpaMapper.toEntity(client);
        ClientJpaEntity saved = jpaRepository.save(entity);
        return ClientJpaMapper.toDomain(saved);
    }

    @Override
    public void delete(Client client) {
        ClientJpaEntity entity = ClientJpaMapper.toEntity(client);
        jpaRepository.delete(entity);
    }

    @Override
    public Optional<Client> findById(UUID id) {
        return jpaRepository.findById(id).map(ClientJpaMapper::toDomain);
    }

    @Override
    public Optional<Client> findClientByDocument(String document) {
        return jpaRepository.findClientByDocument(document).map(ClientJpaMapper::toDomain);
    }

    @Override
    public Optional<Client> findClientByEmail(String email) {
        return jpaRepository.findClientByEmail(email).map(ClientJpaMapper::toDomain);
    }

    @Override
    public ClientPageResult findAll(ClientPageQuery query) {
        Specification<ClientJpaEntity> spec = ClientSpecificationBuilder.buildFilterSpecification(query.filter());
        Sort sort = Sort.by(Sort.Direction.fromString(query.direction()), query.sortBy());
        Pageable pageable = PageRequest.of(query.page(), query.size(), sort);

        var page = jpaRepository.findAll(spec, pageable);
        return new ClientPageResult(page.map(ClientJpaMapper::toDomain).getContent(), page.getTotalElements());
    }
}
