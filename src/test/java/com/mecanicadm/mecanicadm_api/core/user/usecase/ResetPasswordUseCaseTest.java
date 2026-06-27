package com.mecanicadm.mecanicadm_api.core.user.usecase;

import com.mecanicadm.mecanicadm_api.core.user.domain.PasswordResetToken;
import com.mecanicadm.mecanicadm_api.core.user.domain.User;
import com.mecanicadm.mecanicadm_api.core.user.domain.port.PasswordResetTokenGateway;
import com.mecanicadm.mecanicadm_api.core.user.domain.port.UserGateway;
import com.mecanicadm.mecanicadm_api.core.user.exception.UserExceptions;
import com.mecanicadm.mecanicadm_api.core.user.usecase.command.ResetPasswordCommand;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ResetPasswordUseCaseTest {

    @Mock
    private UserGateway userGateway;

    @Mock
    private PasswordResetTokenGateway passwordResetTokenGateway;

    @Mock
    private PasswordEncoder passwordEncoder;

    private ResetPasswordUseCase resetPasswordUseCase;

    @BeforeEach
    void setUp() {
        resetPasswordUseCase = new ResetPasswordUseCase(userGateway, passwordResetTokenGateway, passwordEncoder);
    }

    @Test
    @DisplayName("Deve redefinir a senha com sucesso com um token válido")
    void shouldResetPasswordSuccessfully() {
        String token = "valid-token";
        String newPassword = "newPassword123";
        User user = mock(User.class);
        PasswordResetToken resetToken = mock(PasswordResetToken.class);

        when(passwordResetTokenGateway.findByToken(token)).thenReturn(Optional.of(resetToken));
        when(resetToken.getExpiryDate()).thenReturn(LocalDateTime.now().plusMinutes(10));
        when(resetToken.getUser()).thenReturn(user);

        resetPasswordUseCase.execute(new ResetPasswordCommand(token, newPassword));

        verify(user).changePassword(newPassword, passwordEncoder);
        verify(userGateway).update(user);
        verify(passwordResetTokenGateway).delete(resetToken);
    }

    @Test
    @DisplayName("Deve lançar erro com token expirado")
    void shouldThrowErrorWhenTokenExpired() {
        String token = "expired-token";
        PasswordResetToken resetToken = mock(PasswordResetToken.class);

        when(passwordResetTokenGateway.findByToken(token)).thenReturn(Optional.of(resetToken));
        when(resetToken.getExpiryDate()).thenReturn(LocalDateTime.now().minusMinutes(10));

        assertThrows(UserExceptions.TokenExpired.class, () -> resetPasswordUseCase.execute(new ResetPasswordCommand(token, "pass")));
        
        verify(userGateway, never()).update(any());
    }

    @Test
    @DisplayName("Deve lançar erro com token inválido")
    void shouldThrowErrorWhenTokenInvalid() {
        String token = "invalid-token";
        when(passwordResetTokenGateway.findByToken(token)).thenReturn(Optional.empty());

        assertThrows(UserExceptions.TokenInvalid.class, () -> resetPasswordUseCase.execute(new ResetPasswordCommand(token, "pass")));
    }
}
