package com.mecanicadm.mecanicadm_api.core.user.service;

import com.mecanicadm.mecanicadm_api.core.user.adapter.repository.UserRepository;
import com.mecanicadm.mecanicadm_api.core.user.domain.User;
import com.mecanicadm.mecanicadm_api.core.user.exception.UserExceptions;
import com.mecanicadm.mecanicadm_api.core.user.usecase.query.FindUserByIdQuery;
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
class GetUserByIdUseCaseTest {

    @Mock
    private UserRepository repository;

    @InjectMocks
    private GetUserByIdService getUserByIdUseCase;

    @Test
    @DisplayName("Deve encontrar um usuário por ID com sucesso")
    void shouldFindUserById() {
        UUID id = UUID.randomUUID();
        User user = mock(User.class);
        when(repository.findById(id)).thenReturn(Optional.of(user));

        User result = getUserByIdUseCase.handle(new FindUserByIdQuery(id));

        assertNotNull(result);
        assertEquals(user, result);
        verify(repository).findById(id);
    }

    @Test
    @DisplayName("Deve lançar exceção quando o usuário não existe")
    void shouldThrowExceptionWhenUserNotFound() {
        UUID id = UUID.randomUUID();
        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThrows(UserExceptions.NotFound.class, () -> getUserByIdUseCase.handle(new FindUserByIdQuery(id)));
    }
}
