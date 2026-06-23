package com.mecanicadm.mecanicadm_api.core.workorders.adapter.api;

import com.mecanicadm.mecanicadm_api.testutils.AbstractIntegrationTest;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static com.mecanicadm.mecanicadm_api.testutils.AuthUtils.getAuthToken;
import static org.hamcrest.Matchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class WorkOrderAnalyticsControllerIT extends AbstractIntegrationTest {

    private static final UUID REPORT_SLOWEST_WORK_ORDER_ID = UUID.fromString("960e8400-e29b-41d4-a716-446655440102");
    private static final UUID REPORT_FASTEST_WORK_ORDER_ID = UUID.fromString("960e8400-e29b-41d4-a716-446655440101");

    @Autowired
    private MockMvc mockMvc;

    private String authToken;

    @BeforeEach
    void setUp() {
        RestAssuredMockMvc.mockMvc(mockMvc);
        authToken = getAuthToken("admin@example.com", "password123", "Admin");
    }

    @Test
    @Sql(scripts = "/sql/work_order_execution_time_report_test_data.sql")
    @DisplayName("Deve retornar relatório de tempo de execução das ordens de serviço")
    void shouldGetWorkOrderExecutionTimeReport() {
        RestAssuredMockMvc.given()
                .header("Authorization", "Bearer " + authToken)
                .when()
                .get("/analytics/work-orders/execution-time")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("durationUnit", equalTo("minutes"))
                .body("totalProcessedWorkOrders", equalTo(2))
                .body("averageWorkOrderExecutionTime", is(150.0f))
                .body("slowestWorkOrder.workOrderId", equalTo(REPORT_SLOWEST_WORK_ORDER_ID.toString()))
                .body("fastestWorkOrder.workOrderId", equalTo(REPORT_FASTEST_WORK_ORDER_ID.toString()));
    }

    @Test
    @Sql(scripts = "/sql/work_order_execution_time_report_test_data.sql")
    @DisplayName("Deve retornar relatório de tempo de execução de mão de obra")
    void shouldGetLaborExecutionTimeReport() {
        RestAssuredMockMvc.given()
                .header("Authorization", "Bearer " + authToken)
                .when()
                .get("/analytics/labors/execution-time")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("durationUnit", equalTo("minutes"))
                .body("totalProcessedLabors", equalTo(2))
                .body("averageLaborExecutionTime", is(75.0f))
                .body("statsByLaborType", hasSize(2));
    }

    @Test
    @DisplayName("Deve retornar 400 para período inválido no analytics")
    void shouldReturn400ForInvalidPeriodInAnalytics() {
        RestAssuredMockMvc.given()
                .header("Authorization", "Bearer " + authToken)
                .queryParam("initialDate", "2026-01-20")
                .queryParam("finalDate", "2026-01-10")
                .when()
                .get("/analytics/work-orders/execution-time")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }
}
