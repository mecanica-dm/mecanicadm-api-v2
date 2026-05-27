package com.mecanicadm.mecanicadm_api.core.user.service;

import com.mecanicadm.mecanicadm_api.core.user.adapter.repository.PasswordResetTokenRepository;
import com.mecanicadm.mecanicadm_api.core.user.adapter.repository.UserRepository;
import com.mecanicadm.mecanicadm_api.core.user.domain.PasswordResetToken;
import com.mecanicadm.mecanicadm_api.core.user.domain.User;
import com.mecanicadm.mecanicadm_api.core.user.usecase.command.RequestPasswordResetCommand;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RequestPasswordResetServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordResetTokenRepository passwordResetTokenRepository;

    @InjectMocks
    private RequestPasswordResetService requestPasswordResetService;

    @Test
    @DisplayName("Deve solicitar redefinição de senha com sucesso para e-mail existente")
    void shouldRequestResetSuccessfully() {
        String email = "test@example.com";
        User user = mock(User.class);
        when(userRepository.findUserByEmail(email)).thenReturn(Optional.of(user));

        requestPasswordResetService.handle(new RequestPasswordResetCommand(email));

        verify(passwordResetTokenRepository).deleteByUser(user);
        verify(passwordResetTokenRepository).save(any(PasswordResetToken.class));
    }

    @Test
    @DisplayName("Não deve fazer nada se o e-mail não existir")
    void shouldDoNothingWhenEmailDoesNotExist() {
        String email = "notfound@example.com";
        when(userRepository.findUserByEmail(email)).thenReturn(Optional.empty());

        requestPasswordResetService.handle(new RequestPasswordResetCommand(email));

        verify(passwordResetTokenRepository, never()).deleteByUser(any());
        verify(passwordResetTokenRepository, never()).save(any());
    }
}
