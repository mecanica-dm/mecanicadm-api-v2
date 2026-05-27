package com.mecanicadm.mecanicadm_api.core.material.service;

import com.mecanicadm.mecanicadm_api.core.material.adapter.api.dto.MaterialResponse;
import com.mecanicadm.mecanicadm_api.core.material.adapter.repository.MaterialRepository;
import com.mecanicadm.mecanicadm_api.core.material.exception.MaterialExceptions;
import com.mecanicadm.mecanicadm_api.core.material.usecase.GetMaterialByIdUseCase;
import com.mecanicadm.mecanicadm_api.core.material.usecase.query.GetMaterialByIdQuery;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class GetMaterialByIdService implements GetMaterialByIdUseCase {

    private final MaterialRepository repository;

    public GetMaterialByIdService(MaterialRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional(readOnly = true)
    public MaterialResponse handle(GetMaterialByIdQuery query) {
        return repository.findById(query.id())
                .map(MaterialResponse::new)
                .orElseThrow(MaterialExceptions.MaterialNotFound::new);
    }
}
