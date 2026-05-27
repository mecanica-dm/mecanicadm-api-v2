package com.mecanicadm.mecanicadm_api.core.workorders.service;

import com.mecanicadm.mecanicadm_api.core.workorders.adapter.api.dto.WorkOrderExecutionReportResponse;
import com.mecanicadm.mecanicadm_api.core.workorders.adapter.repository.WorkOrderRepository;
import com.mecanicadm.mecanicadm_api.core.workorders.adapter.repository.projections.WorkOrderExecutionDurationProjection;
import com.mecanicadm.mecanicadm_api.core.workorders.adapter.repository.projections.WorkOrderExecutionSummaryProjection;
import com.mecanicadm.mecanicadm_api.core.workorders.exception.WorkOrderExceptions;
import com.mecanicadm.mecanicadm_api.core.workorders.usecase.query.GetAllWorkOrderExecutionTimeReportQuery;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetAllWorkOrderExecutionTimeReportServiceTest {

    @Mock
    private WorkOrderRepository workOrderRepository;

    @Mock
    private WorkOrderExecutionSummaryProjection summaryProjection;

    @Mock
    private WorkOrderExecutionDurationProjection slowestProjection;

    @Mock
    private WorkOrderExecutionDurationProjection fastestProjection;

    @InjectMocks
    private GetAllWorkOrderExecutionTimeReportService service;

    @Test
    @DisplayName("Deve gerar relatório de tempo de execução com todos os campos")
    void shouldGenerateExecutionTimeReport() {
        UUID slowestId = UUID.randomUUID();
        UUID fastestId = UUID.randomUUID();
        LocalDate initialDate = LocalDate.of(2026, 1, 10);
        LocalDate finalDate = LocalDate.of(2026, 1, 31);

        when(summaryProjection.getTotalProcessedWorkOrders()).thenReturn(4L);
        when(summaryProjection.getAverageExecutionMinutes()).thenReturn(90D);
        when(slowestProjection.getWorkOrderId()).thenReturn(slowestId);
        when(slowestProjection.getDurationMinutes()).thenReturn(180D);
        when(fastestProjection.getWorkOrderId()).thenReturn(fastestId);
        when(fastestProjection.getDurationMinutes()).thenReturn(30D);

        when(workOrderRepository.getExecutionTimeSummary(any(), any())).thenReturn(summaryProjection);
        when(workOrderRepository.getSlowestExecution(any(), any())).thenReturn(slowestProjection);
        when(workOrderRepository.getFastestExecution(any(), any())).thenReturn(fastestProjection);
        when(workOrderRepository.getAverageLaborExecutionMinutes(any(), any())).thenReturn(45D);

        WorkOrderExecutionReportResponse response = service.handle(
                new GetAllWorkOrderExecutionTimeReportQuery(initialDate, finalDate)
        );

        assertNotNull(response);
        assertEquals("minutes", response.durationUnit());
        assertEquals(4L, response.totalProcessedWorkOrders());
        assertEquals(90D, response.averageWorkOrderExecutionTime());
        assertEquals(45D, response.averageWorkOrderLaborExecutionTime());
        assertEquals(slowestId, response.slowestWorkOrder().workOrderId());
        assertEquals(180D, response.slowestWorkOrder().durationInMinutes());
        assertEquals(fastestId, response.fastestWorkOrder().workOrderId());
        assertEquals(30D, response.fastestWorkOrder().durationInMinutes());
    }

    @Test
    @DisplayName("Deve retornar valores padrão quando summary e averageLaborExecutionTime forem nulos")
    void shouldReturnDefaultValuesWhenProjectionsAreNull() {
        when(workOrderRepository.getExecutionTimeSummary(any(), any())).thenReturn(null);
        when(workOrderRepository.getSlowestExecution(any(), any())).thenReturn(null);
        when(workOrderRepository.getFastestExecution(any(), any())).thenReturn(null);
        when(workOrderRepository.getAverageLaborExecutionMinutes(any(), any())).thenReturn(null);

        WorkOrderExecutionReportResponse response = service.handle(
                new GetAllWorkOrderExecutionTimeReportQuery(null, null)
        );

        assertNotNull(response);
        assertEquals(0L, response.totalProcessedWorkOrders());
        assertEquals(0D, response.averageWorkOrderExecutionTime());
        assertEquals(0D, response.averageWorkOrderLaborExecutionTime());
        assertNull(response.slowestWorkOrder());
        assertNull(response.fastestWorkOrder());
    }

    @Test
    @DisplayName("Deve cobrir false hits de campos nulos dentro de summary e averageLaborExecutionTime")
    void shouldCoverFalseHitsForNullFieldsInSummary() {
        when(workOrderRepository.getExecutionTimeSummary(any(), any())).thenReturn(summaryProjection);
        when(summaryProjection.getTotalProcessedWorkOrders()).thenReturn(null);
        when(summaryProjection.getAverageExecutionMinutes()).thenReturn(null);
        when(workOrderRepository.getAverageLaborExecutionMinutes(any(), any())).thenReturn(null);

        WorkOrderExecutionReportResponse response = service.handle(
                new GetAllWorkOrderExecutionTimeReportQuery(LocalDate.now(), LocalDate.now())
        );

        assertEquals(0L, response.totalProcessedWorkOrders());
        assertEquals(0D, response.averageWorkOrderExecutionTime());
        assertEquals(0D, response.averageWorkOrderLaborExecutionTime());
    }

    @Test
    @DisplayName("Deve cobrir false hits para projections com campos nulos")
    void shouldCoverFalseHitsForProjectionsWithNullFields() {
        when(workOrderRepository.getExecutionTimeSummary(any(), any())).thenReturn(null);
        when(workOrderRepository.getSlowestExecution(any(), any())).thenReturn(slowestProjection);
        when(slowestProjection.getWorkOrderId()).thenReturn(null);

        WorkOrderExecutionReportResponse response = service.handle(
                new GetAllWorkOrderExecutionTimeReportQuery(null, null)
        );
        assertNull(response.slowestWorkOrder());

        when(workOrderRepository.getSlowestExecution(any(), any())).thenReturn(fastestProjection);
        when(fastestProjection.getWorkOrderId()).thenReturn(UUID.randomUUID());
        when(fastestProjection.getDurationMinutes()).thenReturn(null);

        response = service.handle(new GetAllWorkOrderExecutionTimeReportQuery(null, null));
        assertNull(response.slowestWorkOrder());
    }

    @Test
    @DisplayName("Não deve lançar exceção quando apenas finalDate for nulo")
    void shouldNotThrowExceptionWhenOnlyFinalDateIsNull() {
        when(workOrderRepository.getExecutionTimeSummary(any(), any())).thenReturn(null);
        assertDoesNotThrow(() -> service.handle(new GetAllWorkOrderExecutionTimeReportQuery(LocalDate.now(), null)));
    }

    @Test
    @DisplayName("Deve lançar exceção quando período informado for inválido")
    void shouldThrowExceptionWhenPeriodIsInvalid() {
        GetAllWorkOrderExecutionTimeReportQuery query =
                new GetAllWorkOrderExecutionTimeReportQuery(LocalDate.of(2026, 2, 1), LocalDate.of(2026, 1, 31));

        assertThrows(WorkOrderExceptions.InvalidReportPeriod.class, () -> service.handle(query));
        verifyNoInteractions(workOrderRepository);
    }
}
