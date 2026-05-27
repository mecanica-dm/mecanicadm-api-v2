package com.mecanicadm.mecanicadm_api.core.user.service;

import com.mecanicadm.mecanicadm_api.core.user.adapter.repository.UserRepository;
import com.mecanicadm.mecanicadm_api.core.user.domain.User;
import com.mecanicadm.mecanicadm_api.core.user.exception.UserExceptions;
import com.mecanicadm.mecanicadm_api.core.user.usecase.command.SoftDeleteUserCommand;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SoftDeleteUserServiceTest {

    @Mock
    private UserRepository repository;

    @InjectMocks
    private SoftDeleteUserService softDeleteUserService;

    @Test
    @DisplayName("Deve deletar um usuário com sucesso")
    void shouldDeleteUser() {
        UUID id = UUID.randomUUID();
        User user = mock(User.class);
        when(repository.findById(id)).thenReturn(Optional.of(user));

        softDeleteUserService.handle(new SoftDeleteUserCommand(id));

        verify(repository).delete(user);
    }

    @Test
    @DisplayName("Deve lançar erro ao deletar usuário inexistente")
    void shouldThrowWhenDeleteNonExistentUser() {
        UUID id = UUID.randomUUID();
        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThrows(UserExceptions.NotFound.class, () -> softDeleteUserService.handle(new SoftDeleteUserCommand(id)));
    }
}
