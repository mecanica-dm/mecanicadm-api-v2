package com.mecanicadm.mecanicadm_api.core.user.service;

import com.mecanicadm.mecanicadm_api.core.user.adapter.repository.PasswordResetTokenRepository;
import com.mecanicadm.mecanicadm_api.core.user.adapter.repository.UserRepository;
import com.mecanicadm.mecanicadm_api.core.user.domain.PasswordResetToken;
import com.mecanicadm.mecanicadm_api.core.user.domain.User;
import com.mecanicadm.mecanicadm_api.core.user.usecase.RequestPasswordResetUseCase;
import com.mecanicadm.mecanicadm_api.core.user.usecase.command.RequestPasswordResetCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class RequestPasswordResetService implements RequestPasswordResetUseCase {

    private static final Logger logger = LoggerFactory.getLogger(RequestPasswordResetService.class);

    private final UserRepository userRepository;
    private final PasswordResetTokenRepository passwordResetTokenRepository;

    public RequestPasswordResetService(UserRepository userRepository,
                                       PasswordResetTokenRepository passwordResetTokenRepository) {
        this.userRepository = userRepository;
        this.passwordResetTokenRepository = passwordResetTokenRepository;
    }

    @Override
    @Transactional
    public void handle(RequestPasswordResetCommand cmd) {
        Optional<User> userOptional = userRepository.findUserByEmail(cmd.email());
        if (userOptional.isEmpty()) {
            return;
        }

        User user = userOptional.get();

        passwordResetTokenRepository.deleteByUser(user);

        String token = UUID.randomUUID().toString();
        PasswordResetToken myToken = new PasswordResetToken(token, user, LocalDateTime.now().plusMinutes(10));
        passwordResetTokenRepository.save(myToken);

        logger.info("""
                            ==========================================================
                            RECUPERAÇÃO DE SENHA SOLICITADA
                            E-mail: {}
                            Token: {}
                            ==========================================================
                        """,
                cmd.email(), token);
    }
}
