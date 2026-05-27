package com.mecanicadm.mecanicadm_api.core.user.service;

import com.mecanicadm.mecanicadm_api.core.user.adapter.repository.UserRepository;
import com.mecanicadm.mecanicadm_api.core.user.domain.User;
import com.mecanicadm.mecanicadm_api.core.user.exception.UserExceptions;
import com.mecanicadm.mecanicadm_api.core.user.usecase.UpdateUserUseCase;
import com.mecanicadm.mecanicadm_api.core.user.usecase.command.UpdateUserCommand;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
public class UpdateUserService implements UpdateUserUseCase {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;

    public UpdateUserService(UserRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void handle(UpdateUserCommand cmd) {
        User user = repository.findById(cmd.id())
                .orElseThrow(UserExceptions.NotFound::new);

        if (StringUtils.hasText(cmd.name())) {
            user.updateInfo(cmd.name(), user.getEmail());
        }

        if (StringUtils.hasText(cmd.password())) {
            if (!StringUtils.hasText(cmd.currentPassword())) {
                throw new UserExceptions.CurrentPasswordRequired();
            }

            user.verifyPassword(cmd.currentPassword(), passwordEncoder);
            user.changePassword(cmd.password(), passwordEncoder);
        }

        repository.save(user);
    }
}