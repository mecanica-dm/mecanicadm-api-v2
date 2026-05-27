package com.mecanicadm.mecanicadm_api.core.workorders.service;

import com.mecanicadm.mecanicadm_api.core.stockmovements.usecase.SoftDeleteStockUseCase;
import com.mecanicadm.mecanicadm_api.core.stockmovements.usecase.command.SoftDeleteStockCommand;
import com.mecanicadm.mecanicadm_api.core.workorders.adapter.repository.WorkOrderRepository;
import com.mecanicadm.mecanicadm_api.core.workorders.domain.WorkOrder;
import com.mecanicadm.mecanicadm_api.core.workorders.exception.WorkOrderExceptions;
import com.mecanicadm.mecanicadm_api.core.workorders.usecase.command.RemoveMaterialItemFromWorkOrderCommand;
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
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RemoveMaterialItemFromWorkOrderServiceTest {

    @Mock
    private WorkOrderRepository workOrderRepository;

    @Mock
    private SoftDeleteStockUseCase softDeleteStockUseCase;

    @InjectMocks
    private RemoveMaterialItemFromWorkOrderService removeMaterialItemFromWorkOrderService;

    private RemoveMaterialItemFromWorkOrderCommand command;
    private UUID workOrderId;
    private UUID materialId;
    private WorkOrder mockWorkOrder;

    @BeforeEach
    void setUp() {
        workOrderId = UUID.randomUUID();
        materialId = UUID.randomUUID();
        command = new RemoveMaterialItemFromWorkOrderCommand(workOrderId, materialId);
        mockWorkOrder = mock(WorkOrder.class);
    }

    @Test
    @DisplayName("Deve remover um item de material da ordem de serviço com sucesso")
    void shouldRemoveMaterialItemFromWorkOrderSuccessfully() {
        when(workOrderRepository.findById(workOrderId)).thenReturn(Optional.of(mockWorkOrder));
        doNothing().when(softDeleteStockUseCase).handle(any(SoftDeleteStockCommand.class));
        doNothing().when(mockWorkOrder).removeMaterialItem(materialId);
        when(workOrderRepository.save(mockWorkOrder)).thenReturn(mockWorkOrder);

        removeMaterialItemFromWorkOrderService.handle(command);

        verify(workOrderRepository).findById(workOrderId);
        verify(softDeleteStockUseCase).handle(new SoftDeleteStockCommand(materialId, workOrderId));
        verify(mockWorkOrder).removeMaterialItem(materialId);
        verify(workOrderRepository).save(mockWorkOrder);
    }

    @Test
    @DisplayName("Deve lançar exceção quando a ordem de serviço não é encontrada")
    void shouldThrowExceptionWhenWorkOrderNotFound() {
        when(workOrderRepository.findById(workOrderId)).thenReturn(Optional.empty());

        assertThrows(WorkOrderExceptions.NotFound.class, () ->
                removeMaterialItemFromWorkOrderService.handle(command));

        verify(workOrderRepository).findById(workOrderId);
        verify(softDeleteStockUseCase, never()).handle(any(SoftDeleteStockCommand.class));
        verify(mockWorkOrder, never()).removeMaterialItem(materialId);
        verify(workOrderRepository, never()).save(any(WorkOrder.class));
    }

    @Test
    @DisplayName("Deve lançar exceção quando o item de material não é encontrado na ordem de serviço")
    void shouldThrowExceptionWhenMaterialItemNotFoundInWorkOrder() {
        when(workOrderRepository.findById(workOrderId)).thenReturn(Optional.of(mockWorkOrder));
        doNothing().when(softDeleteStockUseCase).handle(any(SoftDeleteStockCommand.class));
        doThrow(new WorkOrderExceptions.NotFound()).when(mockWorkOrder).removeMaterialItem(materialId);

        assertThrows(WorkOrderExceptions.NotFound.class, () ->
                removeMaterialItemFromWorkOrderService.handle(command));

        verify(workOrderRepository).findById(workOrderId);
        verify(softDeleteStockUseCase).handle(new SoftDeleteStockCommand(materialId, workOrderId));
        verify(mockWorkOrder).removeMaterialItem(materialId);
        verify(workOrderRepository, never()).save(any(WorkOrder.class));
    }
}