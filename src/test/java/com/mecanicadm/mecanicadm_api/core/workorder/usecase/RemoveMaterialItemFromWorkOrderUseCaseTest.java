package com.mecanicadm.mecanicadm_api.core.workorder.usecase;

import com.mecanicadm.mecanicadm_api.core.stockmovements.usecase.SoftDeleteStockUseCase;
import com.mecanicadm.mecanicadm_api.core.stockmovements.usecase.command.SoftDeleteStockCommand;
import com.mecanicadm.mecanicadm_api.core.workorder.domain.WorkOrder;
import com.mecanicadm.mecanicadm_api.core.workorder.domain.port.WorkOrderGateway;
import com.mecanicadm.mecanicadm_api.core.workorder.exception.WorkOrderExceptions;
import com.mecanicadm.mecanicadm_api.core.workorder.usecase.command.RemoveMaterialItemFromWorkOrderCommand;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RemoveMaterialItemFromWorkOrderUseCaseTest {

    @Mock
    private WorkOrderGateway gateway;

    @Mock
    private SoftDeleteStockUseCase softDeleteStockUseCase;

    @InjectMocks
    private RemoveMaterialItemFromWorkOrderUseCase useCase;

    @Test
    @DisplayName("Deve remover o item de material com sucesso")
    void shouldRemoveMaterialItemSuccessfully() {
        UUID workOrderId = UUID.randomUUID();
        UUID materialId = UUID.randomUUID();
        WorkOrder workOrder = mock(WorkOrder.class);
        when(gateway.findById(workOrderId)).thenReturn(Optional.of(workOrder));

        useCase.execute(new RemoveMaterialItemFromWorkOrderCommand(workOrderId, materialId));

        verify(softDeleteStockUseCase).execute(new SoftDeleteStockCommand(materialId, workOrderId));
        verify(workOrder).removeMaterialItem(materialId);
        verify(gateway).update(workOrder);
    }

    @Test
    @DisplayName("Deve lançar exceção quando a ordem de serviço não for encontrada")
    void shouldThrowExceptionWhenWorkOrderNotFound() {
        UUID workOrderId = UUID.randomUUID();
        UUID materialId = UUID.randomUUID();
        when(gateway.findById(workOrderId)).thenReturn(Optional.empty());

        assertThrows(WorkOrderExceptions.NotFound.class,
                () -> useCase.execute(new RemoveMaterialItemFromWorkOrderCommand(workOrderId, materialId)));

        verify(softDeleteStockUseCase, never()).execute(any());
        verify(gateway, never()).update(any());
    }
}
