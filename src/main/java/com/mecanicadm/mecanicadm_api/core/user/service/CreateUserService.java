package com.mecanicadm.mecanicadm_api.core.user.service;

import com.mecanicadm.mecanicadm_api.core.user.adapter.repository.UserRepository;
import com.mecanicadm.mecanicadm_api.core.user.domain.User;
import com.mecanicadm.mecanicadm_api.core.user.exception.UserExceptions;
import com.mecanicadm.mecanicadm_api.core.user.usecase.CreateUserUseCase;
import com.mecanicadm.mecanicadm_api.core.user.usecase.command.CreateUserCommand;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class CreateUserService implements CreateUserUseCase {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;

    public CreateUserService(UserRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public UUID handle(CreateUserCommand cmd) {
        repository.findUserByEmail(cmd.email()).ifPresent(u -> {
            throw new UserExceptions.EmailExists();
        });

        User newUser = User.create(
                cmd.email(),
                cmd.password(),
                cmd.name(),
                passwordEncoder
        );

        newUser = repository.save(newUser);

        return newUser.getId();
    }

}