package com.mecanicadm.mecanicadm_api.core.labor.service;

import com.mecanicadm.mecanicadm_api.core.labor.adapter.repository.LaborRepository;
import com.mecanicadm.mecanicadm_api.core.labor.exception.LaborExceptions;
import com.mecanicadm.mecanicadm_api.core.labor.usecase.DeleteLaborUseCase;
import com.mecanicadm.mecanicadm_api.core.labor.usecase.command.DeleteLaborCommand;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DeleteLaborService implements DeleteLaborUseCase {

    private final LaborRepository repository;

    public DeleteLaborService(LaborRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional
    public void handle(DeleteLaborCommand cmd) {
        if (!repository.existsById(cmd.id())) {
            throw new LaborExceptions.LaborNotFound();
        }
        repository.deleteById(cmd.id());
    }
}
