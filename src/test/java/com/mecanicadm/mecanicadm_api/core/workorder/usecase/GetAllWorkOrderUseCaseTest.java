package com.mecanicadm.mecanicadm_api.core.workorder.usecase;

import com.mecanicadm.mecanicadm_api.core.workorder.domain.WorkOrder;
import com.mecanicadm.mecanicadm_api.core.workorder.domain.enums.WorkOrderStatus;
import com.mecanicadm.mecanicadm_api.core.workorder.domain.port.SortCriteria;
import com.mecanicadm.mecanicadm_api.core.workorder.domain.port.WorkOrderGateway;
import com.mecanicadm.mecanicadm_api.core.workorder.domain.port.WorkOrderPageQuery;
import com.mecanicadm.mecanicadm_api.core.workorder.domain.port.WorkOrderPageResult;
import com.mecanicadm.mecanicadm_api.core.workorder.usecase.query.GetAllWorkOrdersQuery;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetAllWorkOrderUseCaseTest {

    @Mock
    private WorkOrderGateway gateway;

    @InjectMocks
    private GetAllWorkOrderUseCase useCase;

    @Captor
    private ArgumentCaptor<WorkOrderPageQuery> pageQueryCaptor;

    @Test
    @DisplayName("Deve retornar ordens de serviço paginadas com filtros e sempre aplicar ACTIVE_STATUSES")
    void shouldReturnPaginatedWorkOrdersWithFilters() {
        UUID clientId = UUID.randomUUID();
        String licensePlate = "ABC-1234";
        int page = 0;
        int size = 10;
        String sortBy = "dateCreated";
        String direction = "DESC";
        GetAllWorkOrdersQuery query = new GetAllWorkOrdersQuery(clientId, licensePlate, page, size, sortBy, direction);

        WorkOrderPageResult expectedResult = new WorkOrderPageResult(List.of(mock(WorkOrder.class)), 1L);
        when(gateway.findAll(any())).thenReturn(expectedResult);

        WorkOrderPageResult result = useCase.execute(query);

        assertEquals(expectedResult, result);
        verify(gateway).findAll(pageQueryCaptor.capture());

        var capturedQuery = pageQueryCaptor.getValue();
        assertEquals(clientId, capturedQuery.filter().clientId());
        assertEquals(licensePlate, capturedQuery.filter().licensePlate());
        assertEquals(Set.of(
                WorkOrderStatus.IN_EXECUTION,
                WorkOrderStatus.AWAITING_EXECUTION,
                WorkOrderStatus.DIAGNOSED,
                WorkOrderStatus.RECEIVED
        ), capturedQuery.filter().statuses());
        assertEquals(page, capturedQuery.page());
        assertEquals(size, capturedQuery.size());
        assertEquals(sortBy, capturedQuery.sorts().get(0).field());
        assertEquals(direction, capturedQuery.sorts().get(0).direction());
    }
}
