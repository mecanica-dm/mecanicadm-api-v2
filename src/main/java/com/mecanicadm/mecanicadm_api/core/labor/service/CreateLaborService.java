package com.mecanicadm.mecanicadm_api.core.labor.service;

import com.mecanicadm.mecanicadm_api.core.labor.adapter.repository.LaborRepository;
import com.mecanicadm.mecanicadm_api.core.labor.domain.Labor;
import com.mecanicadm.mecanicadm_api.core.labor.usecase.CreateLaborUseCase;
import com.mecanicadm.mecanicadm_api.core.labor.usecase.command.CreateLaborCommand;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class CreateLaborService implements CreateLaborUseCase {

    private final LaborRepository repository;

    public CreateLaborService(LaborRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional
    public UUID handle(CreateLaborCommand cmd) {
        Labor labor = Labor.create(cmd.name(), cmd.price());
        return repository.save(labor).getId();
    }
}
