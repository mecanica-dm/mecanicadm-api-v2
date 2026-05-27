package com.mecanicadm.mecanicadm_api.core.material.service;

import com.mecanicadm.mecanicadm_api.core.material.adapter.api.dto.MaterialResponse;
import com.mecanicadm.mecanicadm_api.core.material.adapter.repository.MaterialRepository;
import com.mecanicadm.mecanicadm_api.core.material.domain.Material;
import com.mecanicadm.mecanicadm_api.core.material.domain.enums.MaterialType;
import com.mecanicadm.mecanicadm_api.core.material.exception.MaterialExceptions;
import com.mecanicadm.mecanicadm_api.core.material.usecase.query.GetMaterialByIdQuery;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetMaterialByIdServiceTest {

    @Mock
    private MaterialRepository repository;

    @InjectMocks
    private GetMaterialByIdService service;

    @Test
    @DisplayName("Deve retornar um material quando o ID existir")
    void shouldReturnMaterialWhenIdExists() {
        UUID id = UUID.randomUUID();
        Material material = mock(Material.class);
        when(material.getId()).thenReturn(id);
        when(material.getName()).thenReturn("Pneu");
        when(material.getBrand()).thenReturn("Michelin");
        when(material.getPrice()).thenReturn(new BigDecimal("500"));
        when(material.getType()).thenReturn(MaterialType.PART);

        when(repository.findById(id)).thenReturn(Optional.of(material));

        MaterialResponse response = service.handle(new GetMaterialByIdQuery(id));

        assertNotNull(response);
        assertEquals(id, response.id());
        assertEquals("Pneu", response.name());
        verify(repository).findById(id);
    }

    @Test
    @DisplayName("Deve lançar exceção quando o material não for encontrado")
    void shouldThrowExceptionWhenMaterialNotFound() {
        UUID id = UUID.randomUUID();
        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThrows(MaterialExceptions.MaterialNotFound.class, () -> service.handle(new GetMaterialByIdQuery(id)));
        verify(repository).findById(id);
    }
}
