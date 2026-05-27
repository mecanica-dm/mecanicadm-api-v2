package com.mecanicadm.mecanicadm_api.core.user.service;

import com.mecanicadm.mecanicadm_api.core.user.adapter.repository.UserRepository;
import com.mecanicadm.mecanicadm_api.core.user.domain.User;
import com.mecanicadm.mecanicadm_api.core.user.exception.UserExceptions;
import com.mecanicadm.mecanicadm_api.core.user.usecase.command.UpdateUserCommand;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateUserServiceTest {

    @Mock
    private UserRepository repository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UpdateUserService updateUserService;

    @Test
    @DisplayName("Deve atualizar as informações do usuário com sucesso")
    void shouldUpdateUserSuccessfully() {
        UUID id = UUID.randomUUID();
        UpdateUserCommand command = new UpdateUserCommand(id, "Novo Nome", null, null);
        User user = mock(User.class);
        when(user.getEmail()).thenReturn("test@email.com");
        when(repository.findById(id)).thenReturn(Optional.of(user));

        updateUserService.handle(command);

        verify(user).updateInfo("Novo Nome", "test@email.com");
        verify(repository).save(user);
    }

    @Test
    @DisplayName("Deve atualizar a senha do usuário com sucesso")
    void shouldUpdatePasswordSuccessfully() {
        UUID id = UUID.randomUUID();
        String currentPass = "senhaAtual123";
        String newPass = "novaSenha123";
        UpdateUserCommand command = new UpdateUserCommand(id, null, newPass, currentPass);
        User user = mock(User.class);
        when(repository.findById(id)).thenReturn(Optional.of(user));

        updateUserService.handle(command);

        verify(user).verifyPassword(currentPass, passwordEncoder);
        verify(user).changePassword(newPass, passwordEncoder);
        verify(repository).save(user);
    }

    @Test
    @DisplayName("Deve atualizar nome e senha simultaneamente")
    void shouldUpdateNameAndPasswordSimultaneously() {
        UUID id = UUID.randomUUID();
        UpdateUserCommand command = new UpdateUserCommand(id, "Novo Nome", "novaSenha123", "senhaAtual123");
        User user = mock(User.class);
        when(user.getEmail()).thenReturn("test@email.com");
        when(repository.findById(id)).thenReturn(Optional.of(user));

        updateUserService.handle(command);

        verify(user).updateInfo("Novo Nome", "test@email.com");
        verify(user).verifyPassword("senhaAtual123", passwordEncoder);
        verify(user).changePassword("novaSenha123", passwordEncoder);
        verify(repository).save(user);
    }

    @Test
    @DisplayName("Deve lançar erro ao tentar atualizar usuário inexistente")
    void shouldThrowErrorUpdateUserNotFound() {
        UUID id = UUID.randomUUID();
        UpdateUserCommand command = new UpdateUserCommand(id, "Nome", null, null);
        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThrows(UserExceptions.NotFound.class, () -> updateUserService.handle(command));
    }

    @Test
    @DisplayName("Deve lançar erro ao tentar atualizar senha sem fornecer a atual")
    void shouldThrowErrorUpdatePasswordWithoutCurrent() {
        UUID id = UUID.randomUUID();
        UpdateUserCommand command = new UpdateUserCommand(id, null, "novaSenha123", "");
        User user = mock(User.class);
        when(repository.findById(id)).thenReturn(Optional.of(user));

        assertThrows(UserExceptions.CurrentPasswordRequired.class, () -> updateUserService.handle(command));
    }

    @Test
    @DisplayName("Não deve atualizar campos se o comando estiver vazio")
    void shouldNotUpdateAnythingIfCommandIsEmpty() {
        UUID id = UUID.randomUUID();
        UpdateUserCommand command = new UpdateUserCommand(id, "", "", "");
        User user = mock(User.class);
        when(repository.findById(id)).thenReturn(Optional.of(user));

        updateUserService.handle(command);

        verify(user, never()).updateInfo(anyString(), anyString());
        verify(user, never()).changePassword(anyString(), any(PasswordEncoder.class));
        verify(repository).save(user);
    }
}
