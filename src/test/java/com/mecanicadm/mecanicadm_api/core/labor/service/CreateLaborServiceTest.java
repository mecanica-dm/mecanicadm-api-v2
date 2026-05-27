package com.mecanicadm.mecanicadm_api.core.labor.service;

import com.mecanicadm.mecanicadm_api.core.labor.adapter.repository.LaborRepository;
import com.mecanicadm.mecanicadm_api.core.labor.domain.Labor;
import com.mecanicadm.mecanicadm_api.core.labor.usecase.command.CreateLaborCommand;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateLaborServiceTest {

    @Mock
    private LaborRepository repository;

    @InjectMocks
    private CreateLaborService createLaborService;

    private CreateLaborCommand command;

    @BeforeEach
    void setUp() {
        command = new CreateLaborCommand(
                "Troca de Filtro",
                new BigDecimal("40.00")
        );
    }

    @Test
    @DisplayName("Deve criar um serviço com sucesso")
    void shouldCreateLaborSuccessfully() {
        UUID expectedId = UUID.randomUUID();
        Labor savedLabor = mock(Labor.class);
        when(savedLabor.getId()).thenReturn(expectedId);
        when(repository.save(any(Labor.class))).thenReturn(savedLabor);

        UUID resultId = createLaborService.handle(command);

        assertNotNull(resultId);
        assertEquals(expectedId, resultId);
        verify(repository).save(any(Labor.class));
    }
}
