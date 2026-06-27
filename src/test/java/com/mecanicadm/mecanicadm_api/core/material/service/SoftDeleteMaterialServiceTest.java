package com.mecanicadm.mecanicadm_api.core.material.service;

import com.mecanicadm.mecanicadm_api.core.material.domain.port.MaterialGateway;
import com.mecanicadm.mecanicadm_api.core.material.exception.MaterialExceptions;
import com.mecanicadm.mecanicadm_api.core.material.usecase.SoftDeleteMaterialUseCase;
import com.mecanicadm.mecanicadm_api.core.material.usecase.command.SoftDeleteMaterialCommand;
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
class SoftDeleteMaterialServiceTest {

    @Mock
    private MaterialGateway gateway;

    @InjectMocks
    private SoftDeleteMaterialUseCase softDeleteMaterialUseCase;

    @Test
    @DisplayName("Deve deletar um material existente com sucesso")
    void shouldDeleteExistingMaterialSuccessfully() {
        UUID materialId = UUID.randomUUID();
        when(gateway.existsById(materialId)).thenReturn(true);

        softDeleteMaterialUseCase.execute(new SoftDeleteMaterialCommand(materialId));

        verify(gateway).existsById(materialId);
        verify(gateway).deleteById(materialId);
    }

    @Test
    @DisplayName("Deve lançar exceção quando o material não for encontrado para exclusão")
    void shouldThrowExceptionWhenMaterialNotFound() {
        UUID materialId = UUID.randomUUID();
        when(gateway.existsById(materialId)).thenReturn(false);

        assertThrows(MaterialExceptions.MaterialNotFound.class, () -> softDeleteMaterialUseCase.execute(new SoftDeleteMaterialCommand(materialId)));

        verify(gateway).existsById(materialId);
        verify(gateway, never()).deleteById(any());
    }
}
