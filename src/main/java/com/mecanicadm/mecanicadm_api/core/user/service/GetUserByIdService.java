package com.mecanicadm.mecanicadm_api.core.user.service;

import com.mecanicadm.mecanicadm_api.core.user.adapter.repository.UserRepository;
import com.mecanicadm.mecanicadm_api.core.user.domain.User;
import com.mecanicadm.mecanicadm_api.core.user.exception.UserExceptions;
import com.mecanicadm.mecanicadm_api.core.user.usecase.GetUserByIdUseCase;
import com.mecanicadm.mecanicadm_api.core.user.usecase.query.FindUserByIdQuery;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class GetUserByIdService implements GetUserByIdUseCase {

    private final UserRepository repository;

    public GetUserByIdService(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional
    public User handle(FindUserByIdQuery query) {
        return repository.findById(query.id()).orElseThrow(UserExceptions.NotFound::new);
    }
}
