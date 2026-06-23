package com.mecanicadm.mecanicadm_api.core.workorders.service;

import com.mecanicadm.mecanicadm_api.core.workorders.domain.WorkOrder;
import com.mecanicadm.mecanicadm_api.core.workorders.domain.WorkOrderMaterialItem;
import com.mecanicadm.mecanicadm_api.core.workorders.exception.WorkOrderExceptions;
import com.mecanicadm.mecanicadm_api.core.workorders.usecase.RemoveMaterialItemFromWorkOrderUseCase;
import com.mecanicadm.mecanicadm_api.core.workorders.usecase.command.RemoveMaterialItemFromWorkOrderCommand;
import com.mecanicadm.mecanicadm_api.core.workorders.usecase.command.SoftDeleteWorkOrderCommand;
import com.mecanicadm.mecanicadm_api.infra.features.workorder.persistence.jpa.WorkOrderJpaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SoftDeleteWorkOrderServiceTest {

    @Mock
    private WorkOrderJpaRepository repository;

    @Mock
    private RemoveMaterialItemFromWorkOrderUseCase removeMaterialItemFromWorkOrderUseCase;

    @InjectMocks
    private SoftDeleteWorkOrderService softDeleteWorkOrderService;

    @Test
    @DisplayName("Deve deletar uma ordem de serviço existente com sucesso")
    void shouldDeleteExistingWorkOrderSuccessfully() {
        UUID workOrderId = UUID.randomUUID();
        UUID materialId1 = UUID.randomUUID();
        UUID materialId2 = UUID.randomUUID();

        WorkOrderMaterialItem materialItem1 = mock(WorkOrderMaterialItem.class);
        when(materialItem1.getMaterialId()).thenReturn(materialId1);
        WorkOrderMaterialItem materialItem2 = mock(WorkOrderMaterialItem.class);
        when(materialItem2.getMaterialId()).thenReturn(materialId2);

        WorkOrder mockWorkOrder = mock(WorkOrder.class);
        when(mockWorkOrder.getId()).thenReturn(workOrderId);
        when(mockWorkOrder.getMaterialItems()).thenReturn(Set.of(materialItem1, materialItem2));

        when(repository.findById(workOrderId)).thenReturn(java.util.Optional.of(mockWorkOrder));

        softDeleteWorkOrderService.handle(new SoftDeleteWorkOrderCommand(workOrderId));

        verify(repository).findById(workOrderId);
        verify(removeMaterialItemFromWorkOrderUseCase).handle(new RemoveMaterialItemFromWorkOrderCommand(workOrderId, materialId1));
        verify(removeMaterialItemFromWorkOrderUseCase).handle(new RemoveMaterialItemFromWorkOrderCommand(workOrderId, materialId2));
        verify(repository).deleteById(workOrderId);
    }

    @Test
    @DisplayName("Deve lançar exceção quando a ordem de serviço não for encontrada para exclusão")
    void shouldThrowExceptionWhenWorkOrderNotFound() {
        UUID workOrderId = UUID.randomUUID();
        when(repository.findById(workOrderId)).thenReturn(java.util.Optional.empty());

        assertThrows(WorkOrderExceptions.NotFound.class, () -> softDeleteWorkOrderService.handle(new SoftDeleteWorkOrderCommand(workOrderId)));

        verify(repository).findById(workOrderId);
        verify(repository, never()).deleteById(any());
        verify(removeMaterialItemFromWorkOrderUseCase, never()).handle(any());
    }
}