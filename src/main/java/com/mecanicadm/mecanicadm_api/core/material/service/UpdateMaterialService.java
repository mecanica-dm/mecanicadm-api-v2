package com.mecanicadm.mecanicadm_api.core.material.service;

import com.mecanicadm.mecanicadm_api.core.material.adapter.repository.MaterialRepository;
import com.mecanicadm.mecanicadm_api.core.material.domain.Material;
import com.mecanicadm.mecanicadm_api.core.material.exception.MaterialExceptions;
import com.mecanicadm.mecanicadm_api.core.material.usecase.UpdateMaterialUseCase;
import com.mecanicadm.mecanicadm_api.core.material.usecase.command.UpdateMaterialCommand;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UpdateMaterialService implements UpdateMaterialUseCase {

    private final MaterialRepository repository;

    public UpdateMaterialService(MaterialRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional
    public void handle(UpdateMaterialCommand cmd) {
        Material material = repository.findById(cmd.id())
                .orElseThrow(MaterialExceptions.MaterialNotFound::new);

        material.update(
                cmd.name(),
                cmd.brand(),
                cmd.description(),
                cmd.price(),
                cmd.type()
        );

        repository.save(material);
    }
}
