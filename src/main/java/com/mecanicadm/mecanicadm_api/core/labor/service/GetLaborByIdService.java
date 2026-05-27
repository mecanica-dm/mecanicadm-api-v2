package com.mecanicadm.mecanicadm_api.core.labor.service;

import com.mecanicadm.mecanicadm_api.core.labor.adapter.api.dto.LaborResponse;
import com.mecanicadm.mecanicadm_api.core.labor.adapter.repository.LaborRepository;
import com.mecanicadm.mecanicadm_api.core.labor.exception.LaborExceptions;
import com.mecanicadm.mecanicadm_api.core.labor.usecase.GetLaborByIdUseCase;
import com.mecanicadm.mecanicadm_api.core.labor.usecase.query.GetLaborByIdQuery;
import org.springframework.stereotype.Service;

@Service
public class GetLaborByIdService implements GetLaborByIdUseCase {

    private final LaborRepository repository;

    public GetLaborByIdService(LaborRepository repository) {
        this.repository = repository;
    }

    @Override
    public LaborResponse handle(GetLaborByIdQuery query) {
        return repository.findById(query.id())
                .map(labor -> new LaborResponse(
                        labor.getId(),
                        labor.getName(),
                        labor.getPrice(),
                        labor.getDateCreated(),
                        labor.getDateUpdated()
                ))
                .orElseThrow(LaborExceptions.LaborNotFound::new);
    }
}
