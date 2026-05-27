package com.mecanicadm.mecanicadm_api.core.workorders.adapter.api;

import com.mecanicadm.mecanicadm_api.core.workorders.adapter.repository.WorkOrderRepository;
import com.mecanicadm.mecanicadm_api.core.workorders.domain.WorkOrder;
import com.mecanicadm.mecanicadm_api.core.workorders.domain.enums.WorkOrderStatus;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static com.mecanicadm.mecanicadm_api.testutils.AuthUtils.getAuthToken;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class WorkOrderWorkflowControllerIT {

    private static final UUID DIAGNOSE_WORK_ORDER_ID = UUID.fromString("660e8400-e29b-41d4-a716-446655440020");

    private static final UUID WORK_ORDER_READY_TO_START_ID = UUID.fromString("660e8400-e29b-41d4-a716-446655440090");
    private static final UUID WORK_ORDER_IN_EXECUTION_WITH_PENDING_LABOR_ID = UUID.fromString("660e8400-e29b-41d4-a716-446655440091");
    private static final UUID WORK_ORDER_READY_TO_FINISH_ID = UUID.fromString("660e8400-e29b-41d4-a716-446655440093");
    private static final UUID WORK_ORDER_READY_TO_PAY_ID = UUID.fromString("660e8400-e29b-41d4-a716-446655440094");
    private static final UUID WORK_ORDER_READY_TO_DELIVER_ID = UUID.fromString("660e8400-e29b-41d4-a716-446655440095");

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WorkOrderRepository workOrderRepository;

    private String authToken;

    @BeforeEach
    void setUp() {
        RestAssuredMockMvc.mockMvc(mockMvc);
        authToken = getAuthToken("admin@example.com", "password123", "Admin");
    }

    @Test
    @Sql(scripts = "/sql/labor_execution_test_data.sql")
    @DisplayName("Deve iniciar execução da ordem de serviço")
    void shouldStartWorkOrderExecution() {
        RestAssuredMockMvc.given()
                .header("Authorization", "Bearer " + authToken)
                .when()
                .post("/work-orders/{id}/step/start-execution", WORK_ORDER_READY_TO_START_ID)
                .then()
                .statusCode(HttpStatus.NO_CONTENT.value());

        WorkOrder workOrder = workOrderRepository.findById(WORK_ORDER_READY_TO_START_ID).orElseThrow();
        assertEquals(WorkOrderStatus.IN_EXECUTION, workOrder.getStatus());
        assertNotNull(workOrder.getExecutionStartAt());
    }

    @Test
    @Sql(scripts = "/sql/labor_execution_test_data.sql")
    @DisplayName("Deve finalizar execução da ordem de serviço")
    void shouldFinishWorkOrderExecution() {
        RestAssuredMockMvc.given()
                .header("Authorization", "Bearer " + authToken)
                .when()
                .post("/work-orders/{id}/step/finish-execution", WORK_ORDER_READY_TO_FINISH_ID)
                .then()
                .statusCode(HttpStatus.NO_CONTENT.value());

        WorkOrder workOrder = workOrderRepository.findById(WORK_ORDER_READY_TO_FINISH_ID).orElseThrow();
        assertEquals(WorkOrderStatus.EXECUTION_COMPLETED, workOrder.getStatus());
        assertNotNull(workOrder.getExecutionEndAt());
    }

    @Test
    @Sql(scripts = "/sql/labor_execution_test_data.sql")
    @DisplayName("Deve retornar 400 ao finalizar ordem de serviço com mãos de obra pendentes")
    void shouldReturn400WhenFinishingOSWithPendingLabor() {
        RestAssuredMockMvc.given()
                .header("Authorization", "Bearer " + authToken)
                .when()
                .post("/work-orders/{id}/step/finish-execution", WORK_ORDER_IN_EXECUTION_WITH_PENDING_LABOR_ID)
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @Sql(scripts = "/sql/labor_execution_test_data.sql")
    @DisplayName("Deve registrar pagamento da ordem de serviço")
    void shouldRecordWorkOrderPayment() {
        RestAssuredMockMvc.given()
                .header("Authorization", "Bearer " + authToken)
                .when()
                .post("/work-orders/{id}/step/payment", WORK_ORDER_READY_TO_PAY_ID)
                .then()
                .statusCode(HttpStatus.NO_CONTENT.value());

        WorkOrder workOrder = workOrderRepository.findById(WORK_ORDER_READY_TO_PAY_ID).orElseThrow();
        assertEquals(WorkOrderStatus.PAID, workOrder.getStatus());
    }

    @Test
    @Sql(scripts = "/sql/labor_execution_test_data.sql")
    @DisplayName("Deve registrar retirada do veículo")
    void shouldDeliverWorkOrder() {
        RestAssuredMockMvc.given()
                .header("Authorization", "Bearer " + authToken)
                .when()
                .post("/work-orders/{id}/step/deliver", WORK_ORDER_READY_TO_DELIVER_ID)
                .then()
                .statusCode(HttpStatus.NO_CONTENT.value());

        WorkOrder workOrder = workOrderRepository.findById(WORK_ORDER_READY_TO_DELIVER_ID).orElseThrow();
        assertEquals(WorkOrderStatus.DELIVERED, workOrder.getStatus());
    }

    @Test
    @Sql(scripts = "/sql/labor_execution_test_data.sql")
    @DisplayName("Deve retornar 400 ao registrar retirada de veículo sem pagamento")
    void shouldReturn400WhenDeliveringNonPaidWorkOrder() {
        RestAssuredMockMvc.given()
                .header("Authorization", "Bearer " + authToken)
                .when()
                .post("/work-orders/{id}/step/deliver", WORK_ORDER_READY_TO_START_ID)
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @Sql(scripts = "/sql/labor_execution_test_data.sql")
    @DisplayName("Deve retornar 400 ao registrar pagamento de ordem de serviço não finalizada")
    void shouldReturn400WhenRecordingPaymentForNonFinishedOS() {
        RestAssuredMockMvc.given()
                .header("Authorization", "Bearer " + authToken)
                .when()
                .post("/work-orders/{id}/step/payment", WORK_ORDER_READY_TO_START_ID)
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @Sql(scripts = "/sql/work_order_diagnose_test_data.sql")
    @DisplayName("Deve diagnosticar ordem de serviço quando os pré-requisitos forem atendidos")
    void shouldDiagnoseWorkOrderWhenPreconditionsAreMet() {
        String diagnosedWorkOrderId = RestAssuredMockMvc.given()
                .header("Authorization", "Bearer " + authToken)
                .when()
                .post("/work-orders/{id}/step/diagnose", DIAGNOSE_WORK_ORDER_ID)
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract().asString().replace("\"", "").trim();

        assertEquals(DIAGNOSE_WORK_ORDER_ID.toString(), diagnosedWorkOrderId);

        WorkOrderStatus status = workOrderRepository.findById(DIAGNOSE_WORK_ORDER_ID)
                .orElseThrow()
                .getStatus();
        assertEquals(WorkOrderStatus.DIAGNOSED, status);
    }
}