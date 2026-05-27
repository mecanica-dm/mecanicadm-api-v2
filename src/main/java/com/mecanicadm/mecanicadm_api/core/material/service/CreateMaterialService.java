package com.mecanicadm.mecanicadm_api.core.material.service;

import com.mecanicadm.mecanicadm_api.core.material.adapter.repository.MaterialRepository;
import com.mecanicadm.mecanicadm_api.core.material.domain.Material;
import com.mecanicadm.mecanicadm_api.core.material.usecase.CreateMaterialUseCase;
import com.mecanicadm.mecanicadm_api.core.material.usecase.command.CreateMaterialCommand;
import com.mecanicadm.mecanicadm_api.core.stockmovements.usecase.AddStockUseCase;
import com.mecanicadm.mecanicadm_api.core.stockmovements.usecase.command.AddStockCommand;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class CreateMaterialService implements CreateMaterialUseCase {

    private final MaterialRepository repository;
    private final AddStockUseCase addStockUseCase;

    public CreateMaterialService(MaterialRepository repository, AddStockUseCase addStockUseCase) {
        this.repository = repository;
        this.addStockUseCase = addStockUseCase;
    }

    @Override
    @Transactional
    public UUID handle(CreateMaterialCommand cmd) {
        Material material = Material.create(
                cmd.name(),
                cmd.brand(),
                cmd.description(),
                cmd.price(),
                cmd.type()
        );

        material = repository.save(material);

        addStockUseCase.handle(new AddStockCommand(material.getId(), cmd.quantity()));

        return material.getId();
    }
}
