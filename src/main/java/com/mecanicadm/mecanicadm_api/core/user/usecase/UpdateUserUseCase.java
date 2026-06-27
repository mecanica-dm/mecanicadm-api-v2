package com.mecanicadm.mecanicadm_api.core.user.usecase;

import com.mecanicadm.mecanicadm_api.core.user.domain.User;
import com.mecanicadm.mecanicadm_api.core.user.domain.port.UserGateway;
import com.mecanicadm.mecanicadm_api.core.user.exception.UserExceptions;
import com.mecanicadm.mecanicadm_api.core.user.usecase.command.UpdateUserCommand;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

public class UpdateUserUseCase {

    private final UserGateway userGateway;
    private final PasswordEncoder passwordEncoder;

    public UpdateUserUseCase(UserGateway userGateway, PasswordEncoder passwordEncoder) {
        this.userGateway = userGateway;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void execute(UpdateUserCommand command) {
        User user = userGateway.findById(command.id()).orElseThrow(UserExceptions.NotFound::new);

        if (StringUtils.hasText(command.name())) {
            user.updateInfo(command.name(), user.getEmail());
        }

        if (StringUtils.hasText(command.password())) {
            if (!StringUtils.hasText(command.currentPassword())) {
                throw new UserExceptions.CurrentPasswordRequired();
            }

            user.verifyPassword(command.currentPassword(), passwordEncoder);
            user.changePassword(command.password(), passwordEncoder);
        }

        userGateway.update(user);
    }
}
