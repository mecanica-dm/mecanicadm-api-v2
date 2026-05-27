package com.mecanicadm.mecanicadm_api.core.user.service;

import com.mecanicadm.mecanicadm_api.core.user.adapter.repository.UserRepository;
import com.mecanicadm.mecanicadm_api.core.user.domain.User;
import com.mecanicadm.mecanicadm_api.core.user.exception.UserExceptions;
import com.mecanicadm.mecanicadm_api.core.user.usecase.command.CreateUserCommand;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateUserServiceTest {

    @Mock
    private UserRepository repository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private CreateUserService createUserService;

    private CreateUserCommand command;

    @BeforeEach
    void setUp() {
        command = new CreateUserCommand(
                "test@example.com",
                "password123",
                "Test User"
        );
    }

    @Test
    @DisplayName("Deve criar um usuário com sucesso quando o e-mail não existe")
    void shouldCreateUserSuccessfully() {
        when(repository.findUserByEmail(command.email())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(command.password())).thenReturn("encodedPassword");
        
        User savedUser = mock(User.class);
        UUID expectedId = UUID.randomUUID();
        when(savedUser.getId()).thenReturn(expectedId);
        when(repository.save(any(User.class))).thenReturn(savedUser);

        UUID resultId = createUserService.handle(command);

        assertNotNull(resultId);
        assertEquals(expectedId, resultId);
        verify(repository).findUserByEmail(command.email());
        verify(passwordEncoder).encode(command.password());
        verify(repository).save(any(User.class));
    }

    @Test
    @DisplayName("Deve lançar exceção quando o e-mail já está em uso")
    void shouldThrowExceptionWhenEmailExists() {
        when(repository.findUserByEmail(command.email())).thenReturn(Optional.of(mock(User.class)));

        assertThrows(UserExceptions.EmailExists.class, () -> createUserService.handle(command));

        verify(repository).findUserByEmail(command.email());
        verify(repository, never()).save(any(User.class));
    }
}
