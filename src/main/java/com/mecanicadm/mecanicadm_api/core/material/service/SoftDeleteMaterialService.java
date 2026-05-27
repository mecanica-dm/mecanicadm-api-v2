package com.mecanicadm.mecanicadm_api.core.material.service;

import com.mecanicadm.mecanicadm_api.core.material.adapter.repository.MaterialRepository;
import com.mecanicadm.mecanicadm_api.core.material.exception.MaterialExceptions;
import com.mecanicadm.mecanicadm_api.core.material.usecase.SoftDeleteMaterialUseCase;
import com.mecanicadm.mecanicadm_api.core.material.usecase.command.SoftDeleteMaterialCommand;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SoftDeleteMaterialService implements SoftDeleteMaterialUseCase {

    private final MaterialRepository repository;

    public SoftDeleteMaterialService(MaterialRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional
    public void handle(SoftDeleteMaterialCommand cmd) {
        if (!repository.existsById(cmd.id())) {
            throw new MaterialExceptions.MaterialNotFound();
        }
        repository.deleteById(cmd.id());
    }
}
