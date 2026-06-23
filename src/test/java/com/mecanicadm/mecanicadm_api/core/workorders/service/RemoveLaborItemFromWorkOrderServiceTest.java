package com.mecanicadm.mecanicadm_api.core.workorders.service;

import com.mecanicadm.mecanicadm_api.core.workorders.domain.WorkOrderLaborItem;
import com.mecanicadm.mecanicadm_api.core.workorders.exception.WorkOrderExceptions;
import com.mecanicadm.mecanicadm_api.core.workorders.usecase.command.RemoveLaborItemFromWorkOrderCommand;
import com.mecanicadm.mecanicadm_api.infra.features.workorder.persistence.jpa.WorkOrderLaborItemJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RemoveLaborItemFromWorkOrderServiceTest {

    @Mock
    private WorkOrderLaborItemJpaRepository repository;

    @InjectMocks
    private RemoveLaborItemFromWorkOrderService removeLaborItemFromWorkOrderService;

    private RemoveLaborItemFromWorkOrderCommand command;
    private WorkOrderLaborItem mockLaborItem;
    private UUID laborItemId;

    @BeforeEach
    void setUp() {
        laborItemId = UUID.randomUUID();
        command = new RemoveLaborItemFromWorkOrderCommand(UUID.randomUUID(), laborItemId);
        mockLaborItem = mock(WorkOrderLaborItem.class);
    }

    @Test
    @DisplayName("Deve remover um item de trabalho com sucesso quando ele existe")
    void shouldRemoveLaborItemSuccessfullyWhenItExists() {
        when(repository.findById(laborItemId)).thenReturn(Optional.of(mockLaborItem));
        doNothing().when(repository).delete(mockLaborItem);

        removeLaborItemFromWorkOrderService.handle(command);

        verify(repository).findById(laborItemId);
        verify(repository).delete(mockLaborItem);
    }

    @Test
    @DisplayName("Deve lançar exceção quando o item de trabalho não é encontrado")
    void shouldThrowExceptionWhenLaborItemNotFound() {
        when(repository.findById(laborItemId)).thenReturn(Optional.empty());

        assertThrows(WorkOrderExceptions.LaborItemNotFound.class, () ->
                removeLaborItemFromWorkOrderService.handle(command));

        verify(repository).findById(laborItemId);
        verify(repository, never()).delete(any(WorkOrderLaborItem.class));
    }
}
