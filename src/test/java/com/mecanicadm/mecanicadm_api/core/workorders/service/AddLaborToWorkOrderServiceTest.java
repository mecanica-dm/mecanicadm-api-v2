package com.mecanicadm.mecanicadm_api.core.workorders.service;

import com.mecanicadm.mecanicadm_api.core.workorders.adapter.repository.WorkOrderRepository;
import com.mecanicadm.mecanicadm_api.core.workorders.domain.WorkOrder;
import com.mecanicadm.mecanicadm_api.core.workorders.domain.WorkOrderLaborItem;
import com.mecanicadm.mecanicadm_api.core.workorders.exception.WorkOrderExceptions;
import com.mecanicadm.mecanicadm_api.core.workorders.usecase.CreateWorkOrderLaborItemUseCase;
import com.mecanicadm.mecanicadm_api.core.workorders.usecase.command.AddLaborToWorkOrderCommand;
import com.mecanicadm.mecanicadm_api.core.workorders.usecase.command.CreateWorkOrderLaborItemCommand;
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
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AddLaborToWorkOrderServiceTest {

    @Mock
    private WorkOrderRepository workOrderRepository;

    @Mock
    private CreateWorkOrderLaborItemUseCase createWorkOrderLaborItemUseCase;

    @InjectMocks
    private AddLaborToWorkOrderService service;

    @Test
    @DisplayName("Deve adicionar item de mão de obra à ordem de serviço com sucesso")
    void shouldAddLaborToWorkOrderSuccessfully() {
        UUID workOrderId = UUID.randomUUID();
        UUID laborId = UUID.randomUUID();
        AddLaborToWorkOrderCommand command = new AddLaborToWorkOrderCommand(workOrderId, laborId);

        WorkOrder workOrder = spy(WorkOrder.create(UUID.randomUUID(), "ABC-1234", "Descrição do serviço"));
        WorkOrderLaborItem laborItem = WorkOrderLaborItem.create(laborId);

        when(workOrderRepository.findById(workOrderId)).thenReturn(Optional.of(workOrder));
        when(createWorkOrderLaborItemUseCase.handle(any(CreateWorkOrderLaborItemCommand.class))).thenReturn(laborItem);

        service.handle(command);

        verify(workOrderRepository, times(1)).findById(workOrderId);
        verify(createWorkOrderLaborItemUseCase, times(1)).handle(new CreateWorkOrderLaborItemCommand(laborId));
        verify(workOrder, times(1)).addLaborItem(laborItem);
        verify(workOrderRepository, times(1)).save(workOrder);
    }

    @Test
    @DisplayName("Deve lançar exceção quando a ordem de serviço não for encontrada")
    void shouldThrowExceptionWhenWorkOrderNotFound() {
        UUID workOrderId = UUID.randomUUID();
        UUID laborId = UUID.randomUUID();
        AddLaborToWorkOrderCommand command = new AddLaborToWorkOrderCommand(workOrderId, laborId);

        when(workOrderRepository.findById(workOrderId)).thenReturn(Optional.empty());

        assertThrows(WorkOrderExceptions.NotFound.class, () -> service.handle(command));

        verify(workOrderRepository, times(1)).findById(workOrderId);
        verify(createWorkOrderLaborItemUseCase, never()).handle(any());
        verify(workOrderRepository, never()).save(any());
    }
}
