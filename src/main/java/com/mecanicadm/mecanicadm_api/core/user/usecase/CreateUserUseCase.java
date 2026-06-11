package com.mecanicadm.mecanicadm_api.core.user.usecase;

import com.mecanicadm.mecanicadm_api.core.user.domain.User;
import com.mecanicadm.mecanicadm_api.core.user.domain.port.UserGateway;
import com.mecanicadm.mecanicadm_api.core.user.exception.UserExceptions;
import com.mecanicadm.mecanicadm_api.core.user.usecase.command.CreateUserCommand;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.UUID;

public class CreateUserUseCase {

    private final UserGateway userGateway;
    private final PasswordEncoder passwordEncoder;

    public CreateUserUseCase(UserGateway userGateway, PasswordEncoder passwordEncoder) {
        this.userGateway = userGateway;
        this.passwordEncoder = passwordEncoder;
    }

    public UUID execute(CreateUserCommand command) {
        userGateway.findByEmail(command.email()).ifPresent(user -> {
            throw new UserExceptions.UserAlreadyExists();
        });

        User user = User.create(
                command.email(),
                command.password(),
                command.name(),
                passwordEncoder
        );

        User created = userGateway.create(user);
        return created.getId();
    }
}
