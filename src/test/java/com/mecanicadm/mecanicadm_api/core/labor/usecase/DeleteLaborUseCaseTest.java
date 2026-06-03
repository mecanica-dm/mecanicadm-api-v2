package com.mecanicadm.mecanicadm_api.core.labor.usecase;

import com.mecanicadm.mecanicadm_api.core.labor.domain.port.LaborGateway;
import com.mecanicadm.mecanicadm_api.core.labor.exception.LaborExceptions;
import com.mecanicadm.mecanicadm_api.core.labor.usecase.command.DeleteLaborCommand;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeleteLaborUseCaseTest {

    private LaborGateway gateway;

    private DeleteLaborUseCase useCase;

    @BeforeEach
    void setUp() {
        gateway = mock(LaborGateway.class);
        useCase = new DeleteLaborUseCase(gateway);
    }

    @Test
    @DisplayName("Deve deletar um serviço existente com sucesso")
    void shouldDeleteExistingLaborSuccessfully() {
        UUID laborId = UUID.randomUUID();
        DeleteLaborCommand command = new DeleteLaborCommand(laborId);
        when(gateway.existsById(laborId)).thenReturn(true);

        useCase.execute(command);

        verify(gateway).existsById(laborId);
        verify(gateway).deleteById(laborId);
    }

    @Test
    @DisplayName("Deve lançar exceção quando o serviço não for encontrado para exclusão")
    void shouldThrowExceptionWhenLaborNotFound() {
        UUID laborId = UUID.randomUUID();
        DeleteLaborCommand command = new DeleteLaborCommand(laborId);
        when(gateway.existsById(laborId)).thenReturn(false);

        assertThrows(LaborExceptions.LaborNotFound.class, () -> useCase.execute(command));

        verify(gateway).existsById(laborId);
        verify(gateway, never()).deleteById(any());
    }
}
