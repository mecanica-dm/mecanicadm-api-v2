package com.mecanicadm.mecanicadm_api.core.workorder.usecase;

import com.mecanicadm.mecanicadm_api.core.workorder.domain.WorkOrder;
import com.mecanicadm.mecanicadm_api.core.workorder.domain.WorkOrderLaborItem;
import com.mecanicadm.mecanicadm_api.core.workorder.domain.port.WorkOrderGateway;
import com.mecanicadm.mecanicadm_api.core.workorder.exception.WorkOrderExceptions;
import com.mecanicadm.mecanicadm_api.core.workorder.usecase.command.AddLaborToWorkOrderCommand;
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
class AddLaborToWorkOrderUseCaseTest {

    @Mock
    private WorkOrderGateway gateway;

    @Mock
    private CreateWorkOrderLaborItemUseCase createWorkOrderLaborItemUseCase;

    @InjectMocks
    private AddLaborToWorkOrderUseCase useCase;

    @Test
    @DisplayName("Deve adicionar serviço à ordem de serviço com sucesso")
    void shouldAddLaborToWorkOrderSuccessfully() {
        UUID workOrderId = UUID.randomUUID();
        UUID laborId = UUID.randomUUID();
        WorkOrder workOrder = mock(WorkOrder.class);
        WorkOrderLaborItem laborItem = mock(WorkOrderLaborItem.class);
        when(gateway.findById(workOrderId)).thenReturn(Optional.of(workOrder));
        when(createWorkOrderLaborItemUseCase.execute(any())).thenReturn(laborItem);

        useCase.execute(new AddLaborToWorkOrderCommand(workOrderId, laborId));

        verify(gateway).findById(workOrderId);
        verify(workOrder).addLaborItem(laborItem);
        verify(gateway).update(workOrder);
    }

    @Test
    @DisplayName("Deve lançar exceção quando a ordem de serviço não for encontrada")
    void shouldThrowExceptionWhenWorkOrderNotFound() {
        UUID workOrderId = UUID.randomUUID();
        UUID laborId = UUID.randomUUID();
        when(gateway.findById(workOrderId)).thenReturn(Optional.empty());

        assertThrows(WorkOrderExceptions.NotFound.class,
                () -> useCase.execute(new AddLaborToWorkOrderCommand(workOrderId, laborId)));

        verify(gateway).findById(workOrderId);
        verify(gateway, never()).update(any());
    }
}
