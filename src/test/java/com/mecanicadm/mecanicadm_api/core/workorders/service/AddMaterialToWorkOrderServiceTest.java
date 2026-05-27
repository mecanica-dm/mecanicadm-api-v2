package com.mecanicadm.mecanicadm_api.core.workorders.service;

import com.mecanicadm.mecanicadm_api.core.workorders.adapter.repository.WorkOrderRepository;
import com.mecanicadm.mecanicadm_api.core.workorders.domain.WorkOrder;
import com.mecanicadm.mecanicadm_api.core.workorders.domain.WorkOrderMaterialItem;
import com.mecanicadm.mecanicadm_api.core.workorders.exception.WorkOrderExceptions;
import com.mecanicadm.mecanicadm_api.core.workorders.usecase.CreateWorkOrderMaterialItemUseCase;
import com.mecanicadm.mecanicadm_api.core.workorders.usecase.command.AddMaterialToWorkOrderCommand;
import com.mecanicadm.mecanicadm_api.core.workorders.usecase.command.CreateWorkOrderMaterialItemCommand;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AddMaterialToWorkOrderServiceTest {

    @Mock
    private WorkOrderRepository workOrderRepository;

    @Mock
    private CreateWorkOrderMaterialItemUseCase createWorkOrderMaterialItemUseCase;

    @InjectMocks
    private AddMaterialToWorkOrderService addMaterialToWorkOrderService;

    private AddMaterialToWorkOrderCommand command;
    private UUID workOrderId;
    private UUID materialId;
    private int quantity;

    @BeforeEach
    void setUp() {
        workOrderId = UUID.randomUUID();
        materialId = UUID.randomUUID();
        quantity = 2;
        command = new AddMaterialToWorkOrderCommand(workOrderId, materialId, quantity);
    }

    @Test
    @DisplayName("Deve adicionar material à ordem de serviço com sucesso")
    void shouldAddMaterialToWorkOrderSuccessfully() {
        WorkOrder mockWorkOrder = mock(WorkOrder.class);
        WorkOrderMaterialItem mockMaterialItem = mock(WorkOrderMaterialItem.class);

        when(workOrderRepository.findById(workOrderId)).thenReturn(Optional.of(mockWorkOrder));
        when(createWorkOrderMaterialItemUseCase.handle(any(CreateWorkOrderMaterialItemCommand.class)))
                .thenReturn(mockMaterialItem);
        when(workOrderRepository.save(any(WorkOrder.class))).thenReturn(mockWorkOrder);

        addMaterialToWorkOrderService.handle(command);

        verify(workOrderRepository).findById(workOrderId);
        verify(createWorkOrderMaterialItemUseCase).handle(new CreateWorkOrderMaterialItemCommand(workOrderId, materialId, quantity));
        verify(mockWorkOrder).addMaterialItem(mockMaterialItem);
        verify(workOrderRepository).save(mockWorkOrder);
    }

    @Test
    @DisplayName("Deve lançar exceção quando a ordem de serviço não for encontrada")
    void shouldThrowNotFoundWhenWorkOrderDoesNotExist() {
        when(workOrderRepository.findById(workOrderId)).thenReturn(Optional.empty());

        assertThrows(WorkOrderExceptions.NotFound.class, () -> addMaterialToWorkOrderService.handle(command));

        verify(workOrderRepository).findById(workOrderId);
        verify(createWorkOrderMaterialItemUseCase, never()).handle(any(CreateWorkOrderMaterialItemCommand.class));
        verify(workOrderRepository, never()).save(any(WorkOrder.class));
    }
}
