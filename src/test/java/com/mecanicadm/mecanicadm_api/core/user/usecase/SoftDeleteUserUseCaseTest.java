package com.mecanicadm.mecanicadm_api.core.user.usecase;

import com.mecanicadm.mecanicadm_api.core.user.domain.port.UserGateway;
import com.mecanicadm.mecanicadm_api.core.user.usecase.command.SoftDeleteUserCommand;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class SoftDeleteUserUseCaseTest {

    @Mock
    private UserGateway gateway;

    private SoftDeleteUserUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new SoftDeleteUserUseCase(gateway);
    }

    @Test
    @DisplayName("Deve realizar exclusão lógica (soft delete) com sucesso")
    void shouldDeleteUser() {
        UUID id = UUID.randomUUID();
        SoftDeleteUserCommand command = new SoftDeleteUserCommand(id);

        useCase.execute(command);

        verify(gateway).deleteById(id);
    }
}
