package com.mecanicadm.mecanicadm_api.core.material.service;

import com.mecanicadm.mecanicadm_api.core.material.adapter.repository.MaterialRepository;
import com.mecanicadm.mecanicadm_api.core.material.exception.MaterialExceptions;
import com.mecanicadm.mecanicadm_api.core.material.usecase.command.SoftDeleteMaterialCommand;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SoftDeleteMaterialServiceTest {

    @Mock
    private MaterialRepository repository;

    @InjectMocks
    private SoftDeleteMaterialService softDeleteMaterialService;

    @Test
    @DisplayName("Deve deletar um material existente com sucesso")
    void shouldDeleteExistingMaterialSuccessfully() {
        UUID materialId = UUID.randomUUID();
        when(repository.existsById(materialId)).thenReturn(true);

        softDeleteMaterialService.handle(new SoftDeleteMaterialCommand(materialId));

        verify(repository).existsById(materialId);
        verify(repository).deleteById(materialId);
    }

    @Test
    @DisplayName("Deve lançar exceção quando o material não for encontrado para exclusão")
    void shouldThrowExceptionWhenMaterialNotFound() {
        UUID materialId = UUID.randomUUID();
        when(repository.existsById(materialId)).thenReturn(false);

        assertThrows(MaterialExceptions.MaterialNotFound.class, () -> softDeleteMaterialService.handle(new SoftDeleteMaterialCommand(materialId)));

        verify(repository).existsById(materialId);
        verify(repository, never()).deleteById(any());
    }
}
