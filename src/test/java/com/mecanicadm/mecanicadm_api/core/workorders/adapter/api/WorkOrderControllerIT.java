package com.mecanicadm.mecanicadm_api.core.workorders.adapter.api;

import com.mecanicadm.mecanicadm_api.core.workorders.domain.WorkOrder;
import com.mecanicadm.mecanicadm_api.core.workorders.usecase.command.CreateWorkOrderCommand;
import com.mecanicadm.mecanicadm_api.core.workorders.usecase.command.UpdateWorkOrderCommand;
import com.mecanicadm.mecanicadm_api.infra.features.workorder.persistence.jpa.WorkOrderJpaRepository;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static com.mecanicadm.mecanicadm_api.testutils.AuthUtils.getAuthToken;
import static org.hamcrest.Matchers.emptyString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class WorkOrderControllerIT {

    private static final UUID UPDATE_WORK_ORDER_ID = UUID.fromString("660e8400-e29b-41d4-a716-446655440200");
    private static final UUID UPDATE_CLIENT_ID = UUID.fromString("660e8400-e29b-41d4-a716-446655440100");

    private static final UUID FIND_BY_ID_WORK_ORDER_ID = UUID.fromString("660e8400-e29b-41d4-a716-446655440400");
    private static final UUID GET_ALL_CLIENT_ID = UUID.fromString("660e8400-e29b-41d4-a716-446655440300");

    private static final UUID DELETE_WORK_ORDER_ID = UUID.fromString("770e8400-e29b-41d4-a716-446655440400");

    private static final UUID REPORT_SLOWEST_WORK_ORDER_ID = UUID.fromString("960e8400-e29b-41d4-a716-446655440102");
    private static final UUID REPORT_FASTEST_WORK_ORDER_ID = UUID.fromString("960e8400-e29b-41d4-a716-446655440101");

    private static final String MATERIAL_ID = "660e8400-e29b-41d4-a716-446655440402";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WorkOrderJpaRepository workOrderRepository;

    private String authToken;

    @BeforeEach
    void setUp() {
        RestAssuredMockMvc.mockMvc(mockMvc);
        authToken = getAuthToken("admin@example.com", "password123", "Admin");
    }

    @Test
    @DisplayName("Deve criar uma ordem de serviço")
    void shouldCreateWorkOrderWhenAuthenticated() {
        CreateWorkOrderCommand command = new CreateWorkOrderCommand(
                UUID.randomUUID(),
                "ABC-1234",
                "Manutenção Corretiva"
        );

        String workOrderIdStr = RestAssuredMockMvc.given()
                .header("Authorization", "Bearer " + authToken)
                .contentType(MediaType.APPLICATION_JSON)
                .body(command)
                .when()
                .post("/work-orders")
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .body(not(emptyString()))
                .extract().asString().replace("\"", "");

        UUID workOrderId = UUID.fromString(workOrderIdStr);
        assertTrue(workOrderRepository.findById(workOrderId).isPresent());
    }

    @Test
    @Sql(scripts = "/sql/work_order_update_test_data.sql")
    @DisplayName("Deve atualizar uma ordem de serviço")
    void shouldUpdateWorkOrder() {
        UpdateWorkOrderCommand command = new UpdateWorkOrderCommand(
                null,
                "UPD-1234",
                UPDATE_CLIENT_ID,
                "Descrição atualizada"
        );

        RestAssuredMockMvc.given()
                .header("Authorization", "Bearer " + authToken)
                .contentType(MediaType.APPLICATION_JSON)
                .body(command)
                .when()
                .put("/work-orders/{id}", UPDATE_WORK_ORDER_ID)
                .then()
                .statusCode(HttpStatus.OK.value());

        WorkOrder updatedWorkOrder = workOrderRepository.findById(UPDATE_WORK_ORDER_ID).orElseThrow();
        assertEquals("Descrição atualizada", updatedWorkOrder.getDescription());
    }

    @Test
    @Sql(scripts = "/sql/work_order_query_test_data.sql")
    @DisplayName("Deve buscar uma ordem de serviço por ID")
    void shouldFindWorkOrderById() {
        RestAssuredMockMvc.given()
                .header("Authorization", "Bearer " + authToken)
                .when()
                .get("/work-orders/{id}", FIND_BY_ID_WORK_ORDER_ID)
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("id", equalTo(FIND_BY_ID_WORK_ORDER_ID.toString()))
                .body("description", equalTo("Troca de óleo"))
                .body("status", equalTo("RECEIVED"))
                .body("budget.totalPrice", is(150.0f))
                .body("budget.status", equalTo("APPROVED"))
                .body("materialItems", hasSize(1))
                .body("materialItems[0].materialId", equalTo(MATERIAL_ID))
                .body("materialItems[0].quantity", equalTo(2));
    }

    @Test
    @Sql(scripts = "/sql/work_order_query_test_data.sql")
    @DisplayName("Deve listar todas as ordens de serviço")
    void shouldGetAllWorkOrders() {
        RestAssuredMockMvc.given()
                .header("Authorization", "Bearer " + authToken)
                .when()
                .get("/work-orders")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("content", hasSize(2))
                .body("page.totalElements", equalTo(2))
                .body("content.find { it.id == '" + FIND_BY_ID_WORK_ORDER_ID + "' }.budget.totalPrice", is(150.0f))
                .body("content.find { it.id == '" + FIND_BY_ID_WORK_ORDER_ID + "' }.materialItems", hasSize(1));
    }

    @Test
    @Sql(scripts = "/sql/work_order_query_test_data.sql")
    @DisplayName("Deve filtrar ordens de serviço por cliente")
    void shouldFilterWorkOrdersByClientId() {
        RestAssuredMockMvc.given()
                .header("Authorization", "Bearer " + authToken)
                .queryParam("clientId", GET_ALL_CLIENT_ID)
                .when()
                .get("/work-orders")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("content", hasSize(1))
                .body("content[0].clientId", equalTo(GET_ALL_CLIENT_ID.toString()));
    }

    @Test
    @Sql(scripts = "/sql/work_order_query_test_data.sql")
    @DisplayName("Deve filtrar ordens de serviço por placa")
    void shouldFilterWorkOrdersByLicensePlate() {
        RestAssuredMockMvc.given()
                .header("Authorization", "Bearer " + authToken)
                .queryParam("licensePlate", "GET-5678")
                .queryParam("page", 0)
                .queryParam("size", 10)
                .when()
                .get("/work-orders")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("content", hasSize(1))
                .body("content[0].vehicleId", equalTo("GET-5678"));
    }

    @Test
    @Sql(scripts = "/sql/work_order_execution_time_report_test_data.sql")
    @DisplayName("Deve retornar relatório de tempo de execução das ordens de serviço")
    void shouldGetExecutionTimeReport() {
        RestAssuredMockMvc.given()
                .header("Authorization", "Bearer " + authToken)
                .when()
                .get("/analytics/work-orders/execution-time")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("durationUnit", equalTo("minutes"))
                .body("totalProcessedWorkOrders", equalTo(2))
                .body("averageWorkOrderExecutionTime", is(150.0f))
                .body("averageWorkOrderLaborExecutionTime", is(75.0f))
                .body("slowestWorkOrder.workOrderId", equalTo(REPORT_SLOWEST_WORK_ORDER_ID.toString()))
                .body("slowestWorkOrder.durationInMinutes", is(240.0f))
                .body("fastestWorkOrder.workOrderId", equalTo(REPORT_FASTEST_WORK_ORDER_ID.toString()))
                .body("fastestWorkOrder.durationInMinutes", is(60.0f));
    }

    @Test
    @Sql(scripts = "/sql/work_order_execution_time_report_test_data.sql")
    @DisplayName("Deve filtrar relatório por período")
    void shouldFilterExecutionTimeReportByPeriod() {
        RestAssuredMockMvc.given()
                .header("Authorization", "Bearer " + authToken)
                .queryParam("initialDate", "2026-01-12")
                .queryParam("finalDate", "2026-01-12")
                .when()
                .get("/analytics/work-orders/execution-time")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("totalProcessedWorkOrders", equalTo(1))
                .body("averageWorkOrderExecutionTime", is(240.0f))
                .body("averageWorkOrderLaborExecutionTime", is(120.0f))
                .body("slowestWorkOrder.workOrderId", equalTo(REPORT_SLOWEST_WORK_ORDER_ID.toString()))
                .body("fastestWorkOrder.workOrderId", equalTo(REPORT_SLOWEST_WORK_ORDER_ID.toString()));
    }

    @Test
    @DisplayName("Deve retornar 400 quando período do relatório for inválido")
    void shouldReturn400WhenExecutionTimeReportPeriodIsInvalid() {
        RestAssuredMockMvc.given()
                .header("Authorization", "Bearer " + authToken)
                .queryParam("initialDate", "2026-01-20")
                .queryParam("finalDate", "2026-01-10")
                .when()
                .get("/analytics/work-orders/execution-time")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @Sql(scripts = "/sql/work_order_delete_test_data.sql")
    @DisplayName("Deve realizar exclusão lógica (soft delete) da ordem de serviço")
    void shouldSoftDeleteWorkOrder() {
        RestAssuredMockMvc.given()
                .header("Authorization", "Bearer " + authToken)
                .when()
                .delete("/work-orders/{id}", DELETE_WORK_ORDER_ID)
                .then()
                .statusCode(HttpStatus.NO_CONTENT.value());

        assertFalse(workOrderRepository.existsById(DELETE_WORK_ORDER_ID));
    }
}