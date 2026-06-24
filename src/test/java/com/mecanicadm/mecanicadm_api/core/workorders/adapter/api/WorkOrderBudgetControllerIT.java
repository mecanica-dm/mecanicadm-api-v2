package com.mecanicadm.mecanicadm_api.core.workorders.adapter.api;

import com.mecanicadm.mecanicadm_api.core.workorders.domain.WorkOrder;
import com.mecanicadm.mecanicadm_api.core.workorders.domain.WorkOrderBudget;
import com.mecanicadm.mecanicadm_api.core.workorders.domain.enums.WorkOrderBudgetStatus;
import com.mecanicadm.mecanicadm_api.core.workorders.domain.enums.WorkOrderStatus;
import com.mecanicadm.mecanicadm_api.core.workorders.usecase.command.DecideWorkOrderBudgetCommand;
import com.mecanicadm.mecanicadm_api.core.workorders.usecase.command.ManuallyAdjustWorkOrderBudgetCommand;
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

import java.math.BigDecimal;
import java.util.UUID;

import static com.mecanicadm.mecanicadm_api.testutils.AuthUtils.getAuthToken;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class WorkOrderBudgetControllerIT {

    private static final UUID WORK_ORDER_WITH_BUDGET_ID = UUID.fromString("660e8400-e29b-41d4-a716-446655440050");
    private static final UUID RECALCULATE_WORK_ORDER_ID = UUID.fromString("660e8400-e29b-41d4-a716-446655440060");
    private static final UUID DIAGNOSE_WORK_ORDER_ID = UUID.fromString("660e8400-e29b-41d4-a716-446655440020");
    private static final UUID BUDGET_DECISION_WORK_ORDER_ID = UUID.fromString("770e8400-e29b-41d4-a716-446655440010");

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
    @Sql(scripts = "/sql/manually_adjust_budget_test_data.sql")
    @DisplayName("Deve ajustar orçamento manualmente")
    void shouldAdjustBudgetManually() {
        BigDecimal newPrice = new BigDecimal("750.00");
        ManuallyAdjustWorkOrderBudgetCommand command = new ManuallyAdjustWorkOrderBudgetCommand(null, newPrice);

        RestAssuredMockMvc.given()
                .header("Authorization", "Bearer " + authToken)
                .contentType(MediaType.APPLICATION_JSON)
                .body(command)
                .when()
                .patch("/work-orders/{workOrderId}/budget", WORK_ORDER_WITH_BUDGET_ID)
                .then()
                .statusCode(HttpStatus.NO_CONTENT.value());

        WorkOrder workOrder = workOrderRepository.findById(WORK_ORDER_WITH_BUDGET_ID).orElseThrow();
        assertEquals(0, newPrice.compareTo(workOrder.getBudget().map(WorkOrderBudget::getTotalPrice).orElse(BigDecimal.ZERO)));
    }

    @Test
    @Sql(scripts = "/sql/recalculate_budget_test_data.sql")
    @DisplayName("Deve recalcular o orçamento automaticamente")
    void shouldRecalculateBudget() {
        RestAssuredMockMvc.given()
                .header("Authorization", "Bearer " + authToken)
                .when()
                .post("/work-orders/{workOrderId}/budget/recalculate", RECALCULATE_WORK_ORDER_ID)
                .then()
                .statusCode(HttpStatus.NO_CONTENT.value());

        WorkOrder workOrder = workOrderRepository.findById(RECALCULATE_WORK_ORDER_ID).orElseThrow();
        BigDecimal expectedTotal = new BigDecimal("120.00");
        assertEquals(0, expectedTotal.compareTo(workOrder.getBudget().map(WorkOrderBudget::getTotalPrice).orElse(BigDecimal.ZERO)));
    }

    @Test
    @Sql(scripts = "/sql/manually_adjust_budget_test_data.sql")
    @DisplayName("Deve enviar orçamento para o cliente")
    void shouldSendBudgetToClient() {
        RestAssuredMockMvc.given()
                .header("Authorization", "Bearer " + authToken)
                .when()
                .post("/work-orders/{workOrderId}/budget/send", WORK_ORDER_WITH_BUDGET_ID)
                .then()
                .statusCode(HttpStatus.NO_CONTENT.value());

        WorkOrder workOrder = workOrderRepository.findById(WORK_ORDER_WITH_BUDGET_ID).orElseThrow();
        assertEquals(WorkOrderBudgetStatus.WAITING_DECISION, workOrder.getBudget().map(WorkOrderBudget::getStatus).orElseThrow());
    }

    @Test
    @DisplayName("Deve retornar 404 ao enviar orçamento de ordem de serviço inexistente")
    void shouldReturn404WhenSendingBudgetForNonExistingWorkOrder() {
        RestAssuredMockMvc.given()
                .header("Authorization", "Bearer " + authToken)
                .when()
                .post("/work-orders/{workOrderId}/budget/send", UUID.randomUUID())
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    @Sql(scripts = "/sql/work_order_diagnose_test_data.sql")
    @DisplayName("Deve retornar 404 ao enviar orçamento de ordem de serviço sem orçamento gerado")
    void shouldReturn404WhenSendingBudgetForWorkOrderWithoutBudget() {
        RestAssuredMockMvc.given()
                .header("Authorization", "Bearer " + authToken)
                .when()
                .post("/work-orders/{workOrderId}/budget/send", DIAGNOSE_WORK_ORDER_ID)
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    @Sql(scripts = "/sql/decide_work_order_budget_test_data.sql")
    @DisplayName("Deve decidir sobre o orçamento da ordem de serviço")
    void shouldDecideOnWorkOrderBudget() {
        DecideWorkOrderBudgetCommand command = new DecideWorkOrderBudgetCommand(
                BUDGET_DECISION_WORK_ORDER_ID,
                WorkOrderBudgetStatus.APPROVED,
                null
        );

        RestAssuredMockMvc.given()
                .header("Authorization", "Bearer " + authToken)
                .contentType(MediaType.APPLICATION_JSON)
                .body(command)
                .when()
                .post("/work-orders/{workOrderId}/budget/decision", BUDGET_DECISION_WORK_ORDER_ID)
                .then()
                .statusCode(HttpStatus.NO_CONTENT.value());

        WorkOrder workOrder = workOrderRepository.findById(BUDGET_DECISION_WORK_ORDER_ID).orElseThrow();
        assertEquals(WorkOrderStatus.AWAITING_EXECUTION, workOrder.getStatus());
        assertEquals(WorkOrderBudgetStatus.APPROVED, workOrder.getBudget().map(WorkOrderBudget::getStatus).orElseThrow());
    }

    @Test
    @Sql(scripts = "/sql/recalculate_budget_test_data.sql")
    @DisplayName("Deve imprimir o orcamento retornando Base64")
    void shouldPrintBudget() {
        RestAssuredMockMvc.given()
                .header("Authorization", "Bearer " + authToken)
                .when()
                .get("/work-orders/{workOrderId}/budget/print", RECALCULATE_WORK_ORDER_ID)
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("fileName", notNullValue())
                .body("base64Content", notNullValue());
    }
}
