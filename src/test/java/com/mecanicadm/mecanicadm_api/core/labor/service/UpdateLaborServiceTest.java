package com.mecanicadm.mecanicadm_api.core.labor.service;

import com.mecanicadm.mecanicadm_api.core.labor.adapter.repository.LaborRepository;
import com.mecanicadm.mecanicadm_api.core.labor.domain.Labor;
import com.mecanicadm.mecanicadm_api.core.labor.exception.LaborExceptions;
import com.mecanicadm.mecanicadm_api.core.labor.usecase.command.UpdateLaborCommand;
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
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UpdateLaborServiceTest {

    @Mock
    private LaborRepository repository;

    @InjectMocks
    private UpdateLaborService updateLaborService;

    private UUID laborId;
    private UpdateLaborCommand command;
    private Labor existingLabor;

    @BeforeEach
    void setUp() {
        laborId = UUID.randomUUID();
        command = new UpdateLaborCommand(
                laborId,
                "Alinhamento e Balanceamento",
                new BigDecimal("120.00")
        );
        existingLabor = Labor.create(
                "Troca de Óleo",
                new BigDecimal("50.00")
        );
    }

    @Test
    @DisplayName("Deve atualizar um serviço existente com sucesso")
    void shouldUpdateExistingLaborSuccessfully() {
        when(repository.findById(laborId)).thenReturn(Optional.of(existingLabor));
        when(repository.save(any(Labor.class))).thenReturn(existingLabor);

        updateLaborService.handle(command);

        verify(repository).findById(laborId);
        verify(repository).save(existingLabor);

        assertEquals(command.name(), existingLabor.getName());
        assertEquals(command.price(), existingLabor.getPrice());
    }

    @Test
    @DisplayName("Deve lançar exceção quando o serviço não for encontrado para atualização")
    void shouldThrowExceptionWhenLaborNotFound() {
        when(repository.findById(laborId)).thenReturn(Optional.empty());

        assertThrows(LaborExceptions.LaborNotFound.class, () -> updateLaborService.handle(command));

        verify(repository).findById(laborId);
        verify(repository, never()).save(any(Labor.class));
    }
}
