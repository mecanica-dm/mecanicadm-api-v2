package com.mecanicadm.mecanicadm_api.core.labor.service;

import com.mecanicadm.mecanicadm_api.core.labor.adapter.api.dto.LaborResponse;
import com.mecanicadm.mecanicadm_api.core.labor.adapter.repository.LaborRepository;
import com.mecanicadm.mecanicadm_api.core.labor.domain.Labor;
import com.mecanicadm.mecanicadm_api.core.labor.usecase.query.SearchLaborsQuery;
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
class GetAllLaborsServiceTest {

    @Mock
    private LaborRepository repository;

    @InjectMocks
    private GetAllLaborsService service;

    @Test
    @DisplayName("Deve retornar uma página de serviço")
    void shouldReturnPageOfLabors() {
        Pageable pageable = PageRequest.of(0, 10);
        SearchLaborsQuery query = new SearchLaborsQuery("Alinhamento", pageable);
        Labor labor = Labor.create("Alinhamento", new BigDecimal("100"));
        Page<Labor> page = new PageImpl<>(List.of(labor), pageable, 1);

        when(repository.findAll(any(Specification.class), eq(pageable))).thenReturn(page);

        Page<LaborResponse> response = service.handle(query);

        assertNotNull(response);
        assertEquals(1, response.getTotalElements());
        assertEquals("Alinhamento", response.getContent().getFirst().name());
        verify(repository).findAll(any(Specification.class), eq(pageable));
    }

    @Test
    @DisplayName("Deve retornar uma página vazia quando nenhum serviço for encontrado")
    void shouldReturnEmptyPageWhenNoLaborFound() {
        Pageable pageable = PageRequest.of(0, 10);
        SearchLaborsQuery query = new SearchLaborsQuery("Inexistente", pageable);
        Page<Labor> page = new PageImpl<>(List.of(), pageable, 0);

        when(repository.findAll(any(Specification.class), eq(pageable))).thenReturn(page);

        Page<LaborResponse> response = service.handle(query);

        assertNotNull(response);
        assertTrue(response.isEmpty());
        verify(repository).findAll(any(Specification.class), eq(pageable));
    }
}
