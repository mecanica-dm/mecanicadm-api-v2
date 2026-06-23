package com.mecanicadm.mecanicadm_api.core.workorders.service;

import com.mecanicadm.mecanicadm_api.core.workorders.domain.WorkOrder;
import com.mecanicadm.mecanicadm_api.core.workorders.domain.WorkOrderLaborItem;
import com.mecanicadm.mecanicadm_api.core.workorders.exception.WorkOrderExceptions;
import com.mecanicadm.mecanicadm_api.core.workorders.usecase.query.GetWorkOrderLaborItemByIdQuery;
import com.mecanicadm.mecanicadm_api.infra.features.workorder.persistence.jpa.WorkOrderJpaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetWorkOrderLaborItemByIdServiceTest {

    @Mock
    private WorkOrderJpaRepository workOrderRepository;

    @Mock
    private WorkOrder workOrder;

    @Mock
    private WorkOrderLaborItem workOrderLaborItem;

    @InjectMocks
    private GetWorkOrderLaborItemByIdService service;

    @Test
    @DisplayName("Deve retornar WorkOrderLaborItemJpaEntity quando encontrado")
    void shouldReturnWorkOrderLaborItemWhenFound() {
        UUID workOrderId = UUID.randomUUID();
        UUID laborItemId = UUID.randomUUID();

        when(workOrderRepository.findById(workOrderId)).thenReturn(Optional.of(workOrder));
        when(workOrder.findLaborItem(laborItemId)).thenReturn(Optional.of(workOrderLaborItem));

        WorkOrderLaborItem result = service.handle(new GetWorkOrderLaborItemByIdQuery(workOrderId, laborItemId));

        assertNotNull(result);
        assertEquals(workOrderLaborItem, result);
        verify(workOrderRepository, times(1)).findById(workOrderId);
        verify(workOrder, times(1)).findLaborItem(laborItemId);
    }

    @Test
    @DisplayName("Deve lançar WorkOrderExceptions.NotFound quando WorkOrderJpaEntity não for encontrado")
    void shouldThrowNotFoundWhenWorkOrderNotFound() {
        UUID workOrderId = UUID.randomUUID();
        UUID laborItemId = UUID.randomUUID();

        when(workOrderRepository.findById(workOrderId)).thenReturn(Optional.empty());

        assertThrows(WorkOrderExceptions.NotFound.class, () -> service.handle(new GetWorkOrderLaborItemByIdQuery(workOrderId, laborItemId)));
        verify(workOrderRepository, times(1)).findById(workOrderId);
        verify(workOrder, never()).findLaborItem(any());
    }

    @Test
    @DisplayName("Deve lançar WorkOrderExceptions.LaborItemNotFound quando WorkOrderLaborItemJpaEntity não for encontrado")
    void shouldThrowLaborItemNotFoundWhenWorkOrderLaborItemNotFound() {
        UUID workOrderId = UUID.randomUUID();
        UUID laborItemId = UUID.randomUUID();

        when(workOrderRepository.findById(workOrderId)).thenReturn(Optional.of(workOrder));
        when(workOrder.findLaborItem(laborItemId)).thenReturn(Optional.empty());

        assertThrows(WorkOrderExceptions.LaborItemNotFound.class, () -> service.handle(new GetWorkOrderLaborItemByIdQuery(workOrderId, laborItemId)));
        verify(workOrderRepository, times(1)).findById(workOrderId);
        verify(workOrder, times(1)).findLaborItem(laborItemId);
    }
}
