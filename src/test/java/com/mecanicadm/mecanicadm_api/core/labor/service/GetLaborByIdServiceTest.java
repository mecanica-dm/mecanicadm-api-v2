package com.mecanicadm.mecanicadm_api.core.labor.service;

import com.mecanicadm.mecanicadm_api.core.labor.adapter.api.dto.LaborResponse;
import com.mecanicadm.mecanicadm_api.core.labor.adapter.repository.LaborRepository;
import com.mecanicadm.mecanicadm_api.core.labor.domain.Labor;
import com.mecanicadm.mecanicadm_api.core.labor.exception.LaborExceptions;
import com.mecanicadm.mecanicadm_api.core.labor.usecase.query.GetLaborByIdQuery;
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
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetLaborByIdServiceTest {

    @Mock
    private LaborRepository repository;

    @InjectMocks
    private GetLaborByIdService service;

    @Test
    @DisplayName("Deve retornar um serviço quando o ID existir")
    void shouldReturnLaborWhenIdExists() {
        UUID id = UUID.randomUUID();
        Labor labor = mock(Labor.class);
        when(labor.getId()).thenReturn(id);
        when(labor.getName()).thenReturn("Alinhamento");
        when(labor.getPrice()).thenReturn(new BigDecimal("100"));

        when(repository.findById(id)).thenReturn(Optional.of(labor));

        LaborResponse response = service.handle(new GetLaborByIdQuery(id));

        assertNotNull(response);
        assertEquals(id, response.id());
        assertEquals("Alinhamento", response.name());
        verify(repository).findById(id);
    }

    @Test
    @DisplayName("Deve lançar exceção quando o serviço não for encontrado")
    void shouldThrowExceptionWhenLaborNotFound() {
        UUID id = UUID.randomUUID();
        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThrows(LaborExceptions.LaborNotFound.class, () -> service.handle(new GetLaborByIdQuery(id)));
        verify(repository).findById(id);
    }
}
