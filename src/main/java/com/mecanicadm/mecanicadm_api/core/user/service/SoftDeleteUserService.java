package com.mecanicadm.mecanicadm_api.core.user.service;

import com.mecanicadm.mecanicadm_api.core.user.adapter.repository.UserRepository;
import com.mecanicadm.mecanicadm_api.core.user.domain.User;
import com.mecanicadm.mecanicadm_api.core.user.exception.UserExceptions;
import com.mecanicadm.mecanicadm_api.core.user.usecase.SoftDeleteUserUseCase;
import com.mecanicadm.mecanicadm_api.core.user.usecase.command.SoftDeleteUserCommand;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SoftDeleteUserService implements SoftDeleteUserUseCase {

    private final UserRepository repository;

    public SoftDeleteUserService(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional
    public void handle(SoftDeleteUserCommand cmd) {
        User user = repository.findById(cmd.id()).orElseThrow(UserExceptions.NotFound::new);
        repository.delete(user);
    }
}
