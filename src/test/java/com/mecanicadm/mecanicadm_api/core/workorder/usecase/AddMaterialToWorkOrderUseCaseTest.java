package com.mecanicadm.mecanicadm_api.core.workorder.usecase;

import com.mecanicadm.mecanicadm_api.core.workorder.domain.WorkOrder;
import com.mecanicadm.mecanicadm_api.core.workorder.domain.WorkOrderMaterialItem;
import com.mecanicadm.mecanicadm_api.core.workorder.domain.port.WorkOrderGateway;
import com.mecanicadm.mecanicadm_api.core.workorder.exception.WorkOrderExceptions;
import com.mecanicadm.mecanicadm_api.core.workorder.usecase.command.AddMaterialToWorkOrderCommand;
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
class AddMaterialToWorkOrderUseCaseTest {

    @Mock
    private WorkOrderGateway gateway;

    @Mock
    private CreateWorkOrderMaterialItemUseCase createWorkOrderMaterialItemUseCase;

    @InjectMocks
    private AddMaterialToWorkOrderUseCase useCase;

    @Test
    @DisplayName("Deve adicionar material à ordem de serviço com sucesso")
    void shouldAddMaterialToWorkOrderSuccessfully() {
        UUID workOrderId = UUID.randomUUID();
        UUID materialId = UUID.randomUUID();
        int quantity = 2;
        WorkOrder workOrder = mock(WorkOrder.class);
        WorkOrderMaterialItem materialItem = mock(WorkOrderMaterialItem.class);
        when(gateway.findById(workOrderId)).thenReturn(Optional.of(workOrder));
        when(createWorkOrderMaterialItemUseCase.execute(any())).thenReturn(materialItem);

        useCase.execute(new AddMaterialToWorkOrderCommand(workOrderId, materialId, quantity));

        verify(gateway).findById(workOrderId);
        verify(workOrder).addMaterialItem(materialItem);
        verify(gateway).update(workOrder);
    }

    @Test
    @DisplayName("Deve lançar exceção quando a ordem de serviço não for encontrada")
    void shouldThrowExceptionWhenWorkOrderNotFound() {
        UUID workOrderId = UUID.randomUUID();
        UUID materialId = UUID.randomUUID();
        int quantity = 1;
        when(gateway.findById(workOrderId)).thenReturn(Optional.empty());

        assertThrows(WorkOrderExceptions.NotFound.class,
                () -> useCase.execute(new AddMaterialToWorkOrderCommand(workOrderId, materialId, quantity)));

        verify(gateway).findById(workOrderId);
        verify(gateway, never()).update(any());
    }
}
