package com.mecanicadm.mecanicadm_api.core.user.service;

import com.mecanicadm.mecanicadm_api.core.user.adapter.repository.PasswordResetTokenRepository;
import com.mecanicadm.mecanicadm_api.core.user.adapter.repository.UserRepository;
import com.mecanicadm.mecanicadm_api.core.user.domain.PasswordResetToken;
import com.mecanicadm.mecanicadm_api.core.user.domain.User;
import com.mecanicadm.mecanicadm_api.core.user.exception.UserExceptions;
import com.mecanicadm.mecanicadm_api.core.user.usecase.ResetPasswordUseCase;
import com.mecanicadm.mecanicadm_api.core.user.usecase.command.ResetPasswordCommand;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class ResetPasswordService implements ResetPasswordUseCase {

    private final UserRepository userRepository;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final PasswordEncoder passwordEncoder;

    public ResetPasswordService(UserRepository userRepository,
                                PasswordResetTokenRepository passwordResetTokenRepository,
                                PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordResetTokenRepository = passwordResetTokenRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void handle(ResetPasswordCommand cmd) {
        PasswordResetToken resetToken = passwordResetTokenRepository.findByToken(cmd.token())
                .orElseThrow(UserExceptions.TokenInvalid::new);

        if (resetToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new UserExceptions.TokenExpired();
        }

        User user = resetToken.getUser();
        user.changePassword(cmd.newPassword(), passwordEncoder);

        userRepository.save(user);
        passwordResetTokenRepository.delete(resetToken);
    }
}
