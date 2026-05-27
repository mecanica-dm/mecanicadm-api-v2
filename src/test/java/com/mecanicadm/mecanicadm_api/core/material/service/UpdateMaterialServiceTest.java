package com.mecanicadm.mecanicadm_api.core.material.service;

import com.mecanicadm.mecanicadm_api.core.material.adapter.repository.MaterialRepository;
import com.mecanicadm.mecanicadm_api.core.material.domain.Material;
import com.mecanicadm.mecanicadm_api.core.material.domain.enums.MaterialType;
import com.mecanicadm.mecanicadm_api.core.material.exception.MaterialExceptions;
import com.mecanicadm.mecanicadm_api.core.material.usecase.command.UpdateMaterialCommand;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateMaterialServiceTest {

    @Mock
    private MaterialRepository repository;

    @InjectMocks
    private UpdateMaterialService updateMaterialService;

    private UUID materialId;
    private UpdateMaterialCommand command;
    private Material existingMaterial;

    @BeforeEach
    void setUp() {
        materialId = UUID.randomUUID();
        command = new UpdateMaterialCommand(
                materialId,
                "Pneu Novo",
                "Marca Nova",
                "Descrição Nova",
                new BigDecimal("300.00"),
                MaterialType.CONSUMABLE
        );
        existingMaterial = Material.create(
                "Pneu Velho",
                "Marca Velha",
                "Descrição Velha",
                new BigDecimal("200.00"),
                MaterialType.PART
        );
    }

    @Test
    @DisplayName("Deve atualizar um material existente com sucesso")
    void shouldUpdateExistingMaterialSuccessfully() {
        when(repository.findById(materialId)).thenReturn(Optional.of(existingMaterial));
        when(repository.save(any(Material.class))).thenReturn(existingMaterial);

        updateMaterialService.handle(command);

        verify(repository).findById(materialId);
        verify(repository).save(existingMaterial);

        assertEquals(command.name(), existingMaterial.getName());
        assertEquals(command.brand(), existingMaterial.getBrand());
        assertEquals(command.price(), existingMaterial.getPrice());
        assertEquals(command.type(), existingMaterial.getType());
    }

    @Test
    @DisplayName("Deve lançar exceção quando o material não for encontrado para atualização")
    void shouldThrowExceptionWhenMaterialNotFound() {
        when(repository.findById(materialId)).thenReturn(Optional.empty());

        assertThrows(MaterialExceptions.MaterialNotFound.class, () -> updateMaterialService.handle(command));

        verify(repository).findById(materialId);
        verify(repository, never()).save(any(Material.class));
    }
}
