package com.mecanicadm.mecanicadm_api.core.workorders.adapter.api;

import com.mecanicadm.mecanicadm_api.core.labor.domain.LaborExecutionReport;
import com.mecanicadm.mecanicadm_api.core.labor.usecase.GetAllLaborExecutionTimeReportUseCase;
import com.mecanicadm.mecanicadm_api.core.workorders.usecase.GetAllWorkOrderExecutionTimeReportUseCase;
import com.mecanicadm.mecanicadm_api.testutils.AbstractIntegrationTest;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc
class WorkOrderAnalyticsControllerTest extends AbstractIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private GetAllWorkOrderExecutionTimeReportUseCase getAllWorkOrderExecutionTimeReportUseCase;

    @MockitoBean
    private GetAllLaborExecutionTimeReportUseCase getAllLaborExecutionTimeReportUseCase;

    @BeforeEach
    void setUp() {
        RestAssuredMockMvc.mockMvc(mockMvc);
    }

    @Test
    @WithMockUser
    @DisplayName("Deve buscar relatorio de execucao de work orders e retornar 200 OK")
    void shouldGetWorkOrderExecutionReportAndReturn200() {
        var report = mock(com.mecanicadm.mecanicadm_api.core.workorders.adapter.api.dto.WorkOrderExecutionReportResponse.class);
        when(getAllWorkOrderExecutionTimeReportUseCase.handle(any())).thenReturn(report);

        RestAssuredMockMvc.given()
                .when()
                .get("/analytics/work-orders/execution-time")
                .then()
                .statusCode(HttpStatus.OK.value());

        verify(getAllWorkOrderExecutionTimeReportUseCase, times(1)).handle(any());
    }

    @Test
    @WithMockUser
    @DisplayName("Deve buscar relatorio de execucao de labors e retornar 200 OK")
    void shouldGetLaborExecutionReportAndReturn200() {
        var report = mock(LaborExecutionReport.class);
        when(report.durationUnit()).thenReturn("MINUTES");
        when(report.totalProcessedLabors()).thenReturn(5L);
        when(report.averageLaborExecutionTime()).thenReturn(30.0);
        when(getAllLaborExecutionTimeReportUseCase.execute(any())).thenReturn(report);

        RestAssuredMockMvc.given()
                .when()
                .get("/analytics/labors/execution-time")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("durationUnit", org.hamcrest.Matchers.equalTo("MINUTES"));

        verify(getAllLaborExecutionTimeReportUseCase, times(1)).execute(any());
    }
}
