package com.mecanicadm.mecanicadm_api.core.material.service;

import com.mecanicadm.mecanicadm_api.core.material.adapter.api.dto.MaterialResponse;
import com.mecanicadm.mecanicadm_api.core.material.adapter.repository.MaterialRepository;
import com.mecanicadm.mecanicadm_api.core.material.domain.Material;
import com.mecanicadm.mecanicadm_api.core.material.domain.enums.MaterialType;
import com.mecanicadm.mecanicadm_api.core.material.usecase.query.SearchMaterialsQuery;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetAllMaterialsServiceTest {

    @Mock
    private MaterialRepository repository;

    @InjectMocks
    private GetAllMaterialsService service;

    @Test
    @DisplayName("Deve retornar uma página de materiais")
    void shouldReturnPageOfMaterials() {
        Pageable pageable = PageRequest.of(0, 10);
        SearchMaterialsQuery query = new SearchMaterialsQuery("Pneu", null, null, pageable);
        Material material = Material.create("Pneu", "Michelin", "Desc", new BigDecimal("500"), MaterialType.PART);
        Page<Material> page = new PageImpl<>(List.of(material), pageable, 1);

        when(repository.findAll(any(Specification.class), eq(pageable))).thenReturn(page);

        Page<MaterialResponse> response = service.handle(query);

        assertNotNull(response);
        assertEquals(1, response.getTotalElements());
        assertEquals("Pneu", response.getContent().get(0).name());
        verify(repository).findAll(any(Specification.class), eq(pageable));
    }

    @Test
    @DisplayName("Deve retornar uma página vazia quando nenhum material for encontrado")
    void shouldReturnEmptyPageWhenNoMaterialFound() {
        Pageable pageable = PageRequest.of(0, 10);
        SearchMaterialsQuery query = new SearchMaterialsQuery("Inexistente", null, null, pageable);
        Page<Material> page = new PageImpl<>(List.of(), pageable, 0);

        when(repository.findAll(any(Specification.class), eq(pageable))).thenReturn(page);

        Page<MaterialResponse> response = service.handle(query);

        assertNotNull(response);
        assertTrue(response.isEmpty());
        verify(repository).findAll(any(Specification.class), eq(pageable));
    }
}
