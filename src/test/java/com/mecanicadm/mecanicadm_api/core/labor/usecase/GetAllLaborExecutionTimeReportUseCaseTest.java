package com.mecanicadm.mecanicadm_api.core.labor.usecase;

import com.mecanicadm.mecanicadm_api.core.labor.usecase.query.GetAllLaborExecutionTimeReportQuery;
import com.mecanicadm.mecanicadm_api.core.workorders.adapter.repository.projections.LaborExecutionSummaryProjection;
import com.mecanicadm.mecanicadm_api.core.workorders.adapter.repository.projections.LaborTypeStatsProjection;
import com.mecanicadm.mecanicadm_api.core.workorders.exception.WorkOrderExceptions;
import com.mecanicadm.mecanicadm_api.infra.features.labor.api.dto.response.LaborExecutionReportResponse;
import com.mecanicadm.mecanicadm_api.infra.features.workorder.persistence.jpa.WorkOrderLaborItemJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetAllLaborExecutionTimeReportUseCaseTest {

    private WorkOrderLaborItemJpaRepository workOrderLaborItemRepository;

    @Mock
    private LaborExecutionSummaryProjection summaryProjection;

    @Mock
    private LaborTypeStatsProjection laborTypeStatsProjection;

    private GetAllLaborExecutionTimeReportUseCase useCase;

    @BeforeEach
    void setUp() {
        workOrderLaborItemRepository = mock(WorkOrderLaborItemJpaRepository.class);
        useCase = new GetAllLaborExecutionTimeReportUseCase(workOrderLaborItemRepository);
    }

    @Test
    @DisplayName("Deve gerar relatório de mão de obra com estatísticas por tipo")
    void shouldGenerateLaborExecutionReportWithStats() {
        UUID laborId = UUID.randomUUID();
        LocalDate initialDate = LocalDate.of(2026, 1, 10);
        LocalDate finalDate = LocalDate.of(2026, 1, 15);

        when(summaryProjection.getTotalProcessedLabors()).thenReturn(10L);
        when(summaryProjection.getAverageExecutionMinutes()).thenReturn(45.5);

        when(laborTypeStatsProjection.getLaborId()).thenReturn(laborId);
        when(laborTypeStatsProjection.getLaborName()).thenReturn("Troca de Óleo");
        when(laborTypeStatsProjection.getTotalExecutions()).thenReturn(5L);
        when(laborTypeStatsProjection.getAverageExecutionMinutes()).thenReturn(30.0);
        when(laborTypeStatsProjection.getMinExecutionMinutes()).thenReturn(20.0);
        when(laborTypeStatsProjection.getMaxExecutionMinutes()).thenReturn(40.0);

        when(workOrderLaborItemRepository.getExecutionTimeSummary(any(), any())).thenReturn(summaryProjection);
        when(workOrderLaborItemRepository.getStatsByLaborType(any(), any())).thenReturn(List.of(laborTypeStatsProjection));

        LaborExecutionReportResponse response = useCase.execute(new GetAllLaborExecutionTimeReportQuery(initialDate, finalDate));

        assertNotNull(response);
        assertEquals(10L, response.totalProcessedLabors());
        assertEquals(45.5, response.averageLaborExecutionTime());
        assertFalse(response.statsByLaborType().isEmpty());
        assertEquals("Troca de Óleo", response.statsByLaborType().getFirst().laborName());
        assertEquals(laborId, response.statsByLaborType().getFirst().laborId());
    }

    @Test
    @DisplayName("Deve lançar exceção ao informar período inválido")
    void shouldThrowExceptionWhenPeriodIsInvalid() {
        LocalDate initialDate = LocalDate.of(2026, 1, 15);
        LocalDate finalDate = LocalDate.of(2026, 1, 10);
        GetAllLaborExecutionTimeReportQuery query = new GetAllLaborExecutionTimeReportQuery(initialDate, finalDate);

        assertThrows(WorkOrderExceptions.InvalidReportPeriod.class, () -> useCase.execute(query));
        verifyNoInteractions(workOrderLaborItemRepository);
    }

    @Test
    @DisplayName("Deve gerar relatório vazio quando não houver execuções no período")
    void shouldGenerateEmptyReportWhenNoExecutionsFound() {
        when(workOrderLaborItemRepository.getExecutionTimeSummary(any(), any())).thenReturn(null);
        when(workOrderLaborItemRepository.getStatsByLaborType(any(), any())).thenReturn(Collections.emptyList());

        LaborExecutionReportResponse response = useCase.execute(new GetAllLaborExecutionTimeReportQuery(null, null));

        assertNotNull(response);
        assertEquals(0L, response.totalProcessedLabors());
        assertEquals(0.0, response.averageLaborExecutionTime());
        assertTrue(response.statsByLaborType().isEmpty());
    }

    @Test
    @DisplayName("Deve retornar 0.0 para averageLaborExecutionTime quando summary.getAverageExecutionMinutes for nulo")
    void shouldReturnZeroForAverageExecutionTimeWhenSummaryAverageIsNull() {
        LocalDate initialDate = LocalDate.of(2026, 1, 10);
        LocalDate finalDate = LocalDate.of(2026, 1, 15);

        when(summaryProjection.getTotalProcessedLabors()).thenReturn(5L);
        when(summaryProjection.getAverageExecutionMinutes()).thenReturn(null);

        when(workOrderLaborItemRepository.getExecutionTimeSummary(any(), any())).thenReturn(summaryProjection);
        when(workOrderLaborItemRepository.getStatsByLaborType(any(), any())).thenReturn(Collections.emptyList());

        LaborExecutionReportResponse response = useCase.execute(new GetAllLaborExecutionTimeReportQuery(initialDate, finalDate));

        assertNotNull(response);
        assertEquals(5L, response.totalProcessedLabors());
        assertEquals(0.0, response.averageLaborExecutionTime());
        assertTrue(response.statsByLaborType().isEmpty());
    }

    @Test
    @DisplayName("Deve retornar 0L para totalProcessedLabors quando summary.getTotalProcessedLabors for nulo")
    void shouldReturnZeroForTotalProcessedLaborsWhenSummaryTotalIsNull() {
        LocalDate initialDate = LocalDate.of(2026, 1, 10);
        LocalDate finalDate = LocalDate.of(2026, 1, 15);

        when(summaryProjection.getTotalProcessedLabors()).thenReturn(null);
        when(summaryProjection.getAverageExecutionMinutes()).thenReturn(45.5);

        when(workOrderLaborItemRepository.getExecutionTimeSummary(any(), any())).thenReturn(summaryProjection);
        when(workOrderLaborItemRepository.getStatsByLaborType(any(), any())).thenReturn(Collections.emptyList());

        LaborExecutionReportResponse response = useCase.execute(new GetAllLaborExecutionTimeReportQuery(initialDate, finalDate));

        assertNotNull(response);
        assertEquals(0L, response.totalProcessedLabors());
        assertEquals(45.5, response.averageLaborExecutionTime());
        assertTrue(response.statsByLaborType().isEmpty());
    }

    @Test
    @DisplayName("Não deve lançar exceção quando initialDate é nulo e finalDate não é nulo")
    void shouldNotThrowExceptionWhenInitialDateIsNullAndFinalDateIsNotNull() {
        LocalDate finalDate = LocalDate.of(2026, 1, 15);
        GetAllLaborExecutionTimeReportQuery query = new GetAllLaborExecutionTimeReportQuery(null, finalDate);

        when(workOrderLaborItemRepository.getExecutionTimeSummary(any(), any())).thenReturn(null);
        when(workOrderLaborItemRepository.getStatsByLaborType(any(), any())).thenReturn(Collections.emptyList());

        assertDoesNotThrow(() -> useCase.execute(query));
    }

    @Test
    @DisplayName("Não deve lançar exceção quando initialDate não é nulo e finalDate é nulo")
    void shouldNotThrowExceptionWhenInitialDateIsNotNullAndFinalDateIsNull() {
        LocalDate initialDate = LocalDate.of(2026, 1, 10);
        GetAllLaborExecutionTimeReportQuery query = new GetAllLaborExecutionTimeReportQuery(initialDate, null);

        when(workOrderLaborItemRepository.getExecutionTimeSummary(any(), any())).thenReturn(null);
        when(workOrderLaborItemRepository.getStatsByLaborType(any(), any())).thenReturn(Collections.emptyList());

        assertDoesNotThrow(() -> useCase.execute(query));
    }

    @Test
    @DisplayName("Não deve lançar exceção quando initialDate é igual a finalDate")
    void shouldNotThrowExceptionWhenInitialDateIsEqualToFinalDate() {
        LocalDate date = LocalDate.of(2026, 1, 10);
        GetAllLaborExecutionTimeReportQuery query = new GetAllLaborExecutionTimeReportQuery(date, date);

        when(workOrderLaborItemRepository.getExecutionTimeSummary(any(), any())).thenReturn(null);
        when(workOrderLaborItemRepository.getStatsByLaborType(any(), any())).thenReturn(Collections.emptyList());

        assertDoesNotThrow(() -> useCase.execute(query));
    }
}
