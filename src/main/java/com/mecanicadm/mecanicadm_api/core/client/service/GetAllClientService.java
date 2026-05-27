package com.mecanicadm.mecanicadm_api.core.client.service;

import com.mecanicadm.mecanicadm_api.core.client.adapter.api.dto.ClientResponse;
import com.mecanicadm.mecanicadm_api.core.client.adapter.repository.ClientRepository;
import com.mecanicadm.mecanicadm_api.core.client.adapter.repository.specification.ClientSpecification;
import com.mecanicadm.mecanicadm_api.core.client.domain.Client;
import com.mecanicadm.mecanicadm_api.core.client.usecase.GetAllClientUseCase;
import com.mecanicadm.mecanicadm_api.core.client.usecase.query.GetAllClientQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class GetAllClientService implements GetAllClientUseCase {

    private final ClientRepository clientRepository;

    public GetAllClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ClientResponse> handle(GetAllClientQuery query) {
        Specification<Client> spec = Specification.where(ClientSpecification.hasName(query.name()))
                .and(ClientSpecification.hasDocument(query.document()));

        return clientRepository.findAll(spec, query.pageable())
                .map(ClientResponse::fromEntity);
    }
}
