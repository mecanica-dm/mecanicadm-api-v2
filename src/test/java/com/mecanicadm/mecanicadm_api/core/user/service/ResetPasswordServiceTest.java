package com.mecanicadm.mecanicadm_api.core.user.service;

import com.mecanicadm.mecanicadm_api.core.user.adapter.repository.PasswordResetTokenRepository;
import com.mecanicadm.mecanicadm_api.core.user.adapter.repository.UserRepository;
import com.mecanicadm.mecanicadm_api.core.user.domain.PasswordResetToken;
import com.mecanicadm.mecanicadm_api.core.user.domain.User;
import com.mecanicadm.mecanicadm_api.core.user.exception.UserExceptions;
import com.mecanicadm.mecanicadm_api.core.user.usecase.command.ResetPasswordCommand;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ResetPasswordServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordResetTokenRepository passwordResetTokenRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private ResetPasswordService resetPasswordService;

    @Test
    @DisplayName("Deve redefinir a senha com sucesso com um token válido")
    void shouldResetPasswordSuccessfully() {
        String token = "valid-token";
        String newPassword = "newPassword123";
        User user = mock(User.class);
        PasswordResetToken resetToken = mock(PasswordResetToken.class);

        when(passwordResetTokenRepository.findByToken(token)).thenReturn(Optional.of(resetToken));
        when(resetToken.getExpiryDate()).thenReturn(LocalDateTime.now().plusMinutes(10));
        when(resetToken.getUser()).thenReturn(user);

        resetPasswordService.handle(new ResetPasswordCommand(token, newPassword));

        verify(user).changePassword(newPassword, passwordEncoder);
        verify(userRepository).save(user);
        verify(passwordResetTokenRepository).delete(resetToken);
    }

    @Test
    @DisplayName("Deve lançar erro com token expirado")
    void shouldThrowErrorWhenTokenExpired() {
        String token = "expired-token";
        PasswordResetToken resetToken = mock(PasswordResetToken.class);

        when(passwordResetTokenRepository.findByToken(token)).thenReturn(Optional.of(resetToken));
        when(resetToken.getExpiryDate()).thenReturn(LocalDateTime.now().minusMinutes(10));

        assertThrows(UserExceptions.TokenExpired.class, () -> resetPasswordService.handle(new ResetPasswordCommand(token, "pass")));
        
        verify(userRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve lançar erro com token inválido")
    void shouldThrowErrorWhenTokenInvalid() {
        String token = "invalid-token";
        when(passwordResetTokenRepository.findByToken(token)).thenReturn(Optional.empty());

        assertThrows(UserExceptions.TokenInvalid.class, () -> resetPasswordService.handle(new ResetPasswordCommand(token, "pass")));
    }
}
