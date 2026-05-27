package com.mecanicadm.mecanicadm_api.core.labor.service;

import com.mecanicadm.mecanicadm_api.core.labor.adapter.repository.LaborRepository;
import com.mecanicadm.mecanicadm_api.core.labor.domain.Labor;
import com.mecanicadm.mecanicadm_api.core.labor.exception.LaborExceptions;
import com.mecanicadm.mecanicadm_api.core.labor.usecase.UpdateLaborUseCase;
import com.mecanicadm.mecanicadm_api.core.labor.usecase.command.UpdateLaborCommand;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class UpdateLaborService implements UpdateLaborUseCase {

    private final LaborRepository repository;

    public UpdateLaborService(LaborRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional
    public UUID handle(UpdateLaborCommand cmd) {
        Labor labor = repository.findById(cmd.id())
                .orElseThrow(LaborExceptions.LaborNotFound::new);

        labor.update(cmd.name(), cmd.price());
        return repository.save(labor).getId();
    }
}
