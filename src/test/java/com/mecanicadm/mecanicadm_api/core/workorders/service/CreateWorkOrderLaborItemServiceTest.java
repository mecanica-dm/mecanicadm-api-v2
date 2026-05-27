package com.mecanicadm.mecanicadm_api.core.workorders.service;

import com.mecanicadm.mecanicadm_api.core.labor.adapter.repository.LaborRepository;
import com.mecanicadm.mecanicadm_api.core.labor.exception.LaborExceptions;
import com.mecanicadm.mecanicadm_api.core.workorders.domain.WorkOrderLaborItem;
import com.mecanicadm.mecanicadm_api.core.workorders.usecase.command.CreateWorkOrderLaborItemCommand;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateWorkOrderLaborItemServiceTest {

    @Mock
    private LaborRepository laborRepository;

    @InjectMocks
    private CreateWorkOrderLaborItemService createWorkOrderLaborItemService;

    private CreateWorkOrderLaborItemCommand command;
    private UUID laborId;

    @BeforeEach
    void setUp() {
        laborId = UUID.randomUUID();
        command = new CreateWorkOrderLaborItemCommand(laborId);
    }

    @Test
    @DisplayName("Deve criar um item de mão de obra da ordem de serviço com sucesso")
    void shouldCreateWorkOrderLaborItemSuccessfully() {
        when(laborRepository.existsById(laborId)).thenReturn(true);

        WorkOrderLaborItem result = createWorkOrderLaborItemService.handle(command);

        assertNotNull(result);
        assertEquals(laborId, result.getLaborId());
        verify(laborRepository).existsById(laborId);
    }

    @Test
    @DisplayName("Deve lançar exceção quando a mão de obra não for encontrada")
    void shouldThrowExceptionWhenLaborNotFound() {
        when(laborRepository.existsById(laborId)).thenReturn(false);

        assertThrows(LaborExceptions.LaborNotFound.class, () -> createWorkOrderLaborItemService.handle(command));
        verify(laborRepository).existsById(laborId);
    }
}
