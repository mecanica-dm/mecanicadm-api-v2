package com.mecanicadm.mecanicadm_api.core.labor.service;

import com.mecanicadm.mecanicadm_api.core.labor.adapter.repository.LaborRepository;
import com.mecanicadm.mecanicadm_api.core.labor.exception.LaborExceptions;
import com.mecanicadm.mecanicadm_api.core.labor.usecase.command.DeleteLaborCommand;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DeleteLaborServiceTest {

    @Mock
    private LaborRepository repository;

    @InjectMocks
    private DeleteLaborService deleteLaborService;

    @Test
    @DisplayName("Deve deletar um serviço existente com sucesso")
    void shouldDeleteExistingLaborSuccessfully() {
        UUID laborId = UUID.randomUUID();
        DeleteLaborCommand command = new DeleteLaborCommand(laborId);
        when(repository.existsById(laborId)).thenReturn(true);

        deleteLaborService.handle(command);

        verify(repository).existsById(laborId);
        verify(repository).deleteById(laborId);
    }

    @Test
    @DisplayName("Deve lançar exceção quando o serviço não for encontrado para exclusão")
    void shouldThrowExceptionWhenLaborNotFound() {
        UUID laborId = UUID.randomUUID();
        DeleteLaborCommand command = new DeleteLaborCommand(laborId);
        when(repository.existsById(laborId)).thenReturn(false);

        assertThrows(LaborExceptions.LaborNotFound.class, () -> deleteLaborService.handle(command));

        verify(repository).existsById(laborId);
        verify(repository, never()).deleteById(any());
    }
}
