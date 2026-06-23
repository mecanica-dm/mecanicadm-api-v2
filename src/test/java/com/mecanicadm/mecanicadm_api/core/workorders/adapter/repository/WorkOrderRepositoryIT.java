package com.mecanicadm.mecanicadm_api.core.workorders.adapter.repository;

import com.mecanicadm.mecanicadm_api.core.workorders.adapter.repository.projections.WorkOrderExecutionDurationProjection;
import com.mecanicadm.mecanicadm_api.core.workorders.adapter.repository.projections.WorkOrderExecutionSummaryProjection;
import com.mecanicadm.mecanicadm_api.testutils.AbstractIntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class WorkOrderRepositoryIT extends AbstractIntegrationTest {

    @Autowired
    private WorkOrderRepository workOrderRepository;

    private final UUID workOrderId = UUID.fromString("550e8400-e29b-41d4-a716-446655440005");

    @Test
    @Sql(scripts = "/sql/work_order_budget_test_data.sql")
    @DisplayName("Deve somar corretamente o total de materiais de uma ordem de serviço")
    void shouldSumMaterialsTotal() {
        BigDecimal total = workOrderRepository.sumMaterialsTotalByWorkOrderId(workOrderId);

        assertEquals(0, new BigDecimal("130.00").compareTo(total));
    }

    @Test
    @Sql(scripts = "/sql/work_order_budget_test_data.sql")
    @DisplayName("Deve somar corretamente o total de mão de obra de uma ordem de serviço")
    void shouldSumLaborTotal() {
        BigDecimal total = workOrderRepository.sumLaborTotalByWorkOrderId(workOrderId);

        assertEquals(0, new BigDecimal("200.00").compareTo(total));
    }

    @Test
    @DisplayName("Deve retornar zero quando a ordem de serviço não possuir itens")
    void shouldReturnZeroWhenNoItems() {
        UUID randomId = UUID.randomUUID();
        
        BigDecimal totalMaterials = workOrderRepository.sumMaterialsTotalByWorkOrderId(randomId);
        BigDecimal totalLabor = workOrderRepository.sumLaborTotalByWorkOrderId(randomId);

        assertEquals(0, BigDecimal.ZERO.compareTo(totalMaterials));
        assertEquals(0, BigDecimal.ZERO.compareTo(totalLabor));
    }

    @Test
    @Sql(scripts = "/sql/work_order_execution_time_report_test_data.sql")
    @DisplayName("Deve retornar resumo de execução com ordens finalizadas")
    void shouldReturnExecutionSummary() {
        WorkOrderExecutionSummaryProjection summary =
                workOrderRepository.getExecutionTimeSummary(null, null);

        assertNotNull(summary);
        assertEquals(2L, summary.getTotalProcessedWorkOrders());
        assertEquals(150D, summary.getAverageExecutionMinutes());
    }

    @Test
    @Sql(scripts = "/sql/work_order_execution_time_report_test_data.sql")
    @DisplayName("Deve retornar ordens mais lenta e mais rápida")
    void shouldReturnSlowestAndFastestExecution() {
        WorkOrderExecutionDurationProjection slowest =
                workOrderRepository.getSlowestExecution(null, null);
        WorkOrderExecutionDurationProjection fastest =
                workOrderRepository.getFastestExecution(null, null);

        assertNotNull(slowest);
        assertNotNull(fastest);
        assertEquals("960e8400-e29b-41d4-a716-446655440102", slowest.getWorkOrderId().toString());
        assertEquals(240D, slowest.getDurationMinutes());
        assertEquals("960e8400-e29b-41d4-a716-446655440101", fastest.getWorkOrderId().toString());
        assertEquals(60D, fastest.getDurationMinutes());
    }

    @Test
    @Sql(scripts = "/sql/work_order_execution_time_report_test_data.sql")
    @DisplayName("Deve filtrar métricas de execução por período")
    void shouldFilterExecutionMetricsByPeriod() {
        LocalDateTime initialDate = LocalDateTime.of(2026, 1, 12, 0, 0);
        LocalDateTime finalDateExclusive = LocalDateTime.of(2026, 1, 13, 0, 0);

        WorkOrderExecutionSummaryProjection summary =
                workOrderRepository.getExecutionTimeSummary(initialDate, finalDateExclusive);
        Double averageLabor = workOrderRepository.getAverageLaborExecutionMinutes(initialDate, finalDateExclusive);

        assertNotNull(summary);
        assertEquals(1L, summary.getTotalProcessedWorkOrders());
        assertEquals(240D, summary.getAverageExecutionMinutes());
        assertNotNull(averageLabor);
        assertTrue(averageLabor > 0);
        assertEquals(120D, averageLabor);
    }
}
