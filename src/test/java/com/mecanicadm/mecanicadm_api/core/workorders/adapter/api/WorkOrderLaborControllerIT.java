package com.mecanicadm.mecanicadm_api.core.workorders.adapter.api;

import com.mecanicadm.mecanicadm_api.core.workorders.domain.WorkOrder;
import com.mecanicadm.mecanicadm_api.core.workorders.domain.WorkOrderLaborItem;
import com.mecanicadm.mecanicadm_api.testutils.AbstractIntegrationTest;
import com.mecanicadm.mecanicadm_api.core.workorders.domain.enums.LaborExecutionStatus;
import com.mecanicadm.mecanicadm_api.infra.features.workorder.persistence.jpa.WorkOrderJpaRepository;
import com.mecanicadm.mecanicadm_api.infra.features.workorder.persistence.jpa.WorkOrderLaborItemJpaRepository;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.mecanicadm.mecanicadm_api.testutils.AuthUtils.getAuthToken;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class WorkOrderLaborControllerIT extends AbstractIntegrationTest {

    private static final UUID WORK_ORDER_READY_TO_START_ID = UUID.fromString("660e8400-e29b-41d4-a716-446655440090");
    private static final UUID WORK_ORDER_IN_EXECUTION_ID = UUID.fromString("660e8400-e29b-41d4-a716-446655440091");
    private static final UUID LABOR_AWAITING_EXECUTION_ID = UUID.fromString("660e8400-e29b-41d4-a716-446655440086");
    private static final UUID WORK_ORDER_WITH_LABOR_IN_EXECUTION_ID = UUID.fromString("660e8400-e29b-41d4-a716-446655440092");
    private static final UUID LABOR_IN_EXECUTION_ID = UUID.fromString("660e8400-e29b-41d4-a716-446655440087");

    private static final UUID WORK_ORDER_ID = UUID.fromString("660e8400-e29b-41d4-a716-446655440420");
    private static final UUID LABOR_ITEM_TO_REMOVE_ID = UUID.fromString("660e8400-e29b-41d4-a716-446655440101");

    private static final UUID ADD_LABOR_WORK_ORDER_ID = UUID.fromString("660e8400-e29b-41d4-a716-446655440096");
    private static final UUID ADD_LABOR_ID = UUID.fromString("550e8400-e29b-41d4-a716-446655440003");

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WorkOrderJpaRepository workOrderRepository;

    @Autowired
    private WorkOrderLaborItemJpaRepository workOrderLaborItemRepository;

    private String authToken;

    @BeforeEach
    void setUp() {
        RestAssuredMockMvc.mockMvc(mockMvc);
        authToken = getAuthToken("admin@example.com", "password123", "Admin");
    }

    @Test
    @Sql(scripts = "/sql/labor_execution_test_data.sql")
    @DisplayName("Deve iniciar execução de mão de obra quando ordem de serviço estiver em execução")
    void shouldStartLaborExecution() {
        RestAssuredMockMvc.given()
                .header("Authorization", "Bearer " + authToken)
                .when()
                .post("/work-orders/{workOrderId}/labors/{laborItemId}/start", WORK_ORDER_IN_EXECUTION_ID, LABOR_AWAITING_EXECUTION_ID)
                .then()
                .statusCode(HttpStatus.NO_CONTENT.value());

        WorkOrder workOrder = workOrderRepository.findById(WORK_ORDER_IN_EXECUTION_ID).orElseThrow();
        WorkOrderLaborItem laborItem = workOrder.findLaborItem(LABOR_AWAITING_EXECUTION_ID).orElseThrow();
        assertEquals(LaborExecutionStatus.IN_EXECUTION, laborItem.getStatus());
        assertNotNull(laborItem.getExecutionStartAt());
    }

    @Test
    @Sql(scripts = "/sql/labor_execution_test_data.sql")
    @DisplayName("Deve retornar 400 ao iniciar labor se ordem de serviço não estiver em execução")
    void shouldReturn400WhenStartingLaborAndOSNotInProgress() {
        RestAssuredMockMvc.given()
                .header("Authorization", "Bearer " + authToken)
                .when()
                .post("/work-orders/{workOrderId}/labors/{laborItemId}/start", WORK_ORDER_READY_TO_START_ID, UUID.fromString("660e8400-e29b-41d4-a716-446655440085"))
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @Sql(scripts = "/sql/labor_execution_test_data.sql")
    @DisplayName("Deve finalizar execução de mão de obra")
    void shouldFinishLaborExecution() {
        RestAssuredMockMvc.given()
                .header("Authorization", "Bearer " + authToken)
                .when()
                .post("/work-orders/{workOrderId}/labors/{laborItemId}/finish", WORK_ORDER_WITH_LABOR_IN_EXECUTION_ID, LABOR_IN_EXECUTION_ID)
                .then()
                .statusCode(HttpStatus.NO_CONTENT.value());

        WorkOrder workOrder = workOrderRepository.findById(WORK_ORDER_WITH_LABOR_IN_EXECUTION_ID).orElseThrow();
        WorkOrderLaborItem laborItem = workOrder.findLaborItem(LABOR_IN_EXECUTION_ID).orElseThrow();
        assertEquals(LaborExecutionStatus.EXECUTION_COMPLETED, laborItem.getStatus());
        assertNotNull(laborItem.getExecutionEndAt());
    }

    @Test
    @Sql(scripts = "/sql/labor_execution_test_data.sql")
    @DisplayName("Deve buscar um item de mão de obra por ID")
    void shouldFindWorkOrderLaborItemById() {
        RestAssuredMockMvc.given()
                .header("Authorization", "Bearer " + authToken)
                .when()
                .get("/work-orders/{workOrderId}/labors/{laborItemId}", WORK_ORDER_IN_EXECUTION_ID, LABOR_AWAITING_EXECUTION_ID)
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("id", equalTo(LABOR_AWAITING_EXECUTION_ID.toString()))
                .body("status", equalTo("AWAITING_EXECUTION"));
    }

    @Test
    @Sql(scripts = "/sql/remove_labor_item_test_data.sql")
    @DisplayName("Deve remover um item de mão de obra de uma ordem de serviço")
    void shouldRemoveLaborItemFromWorkOrder() {
        RestAssuredMockMvc.given()
                .header("Authorization", "Bearer " + authToken)
                .when()
                .post("/work-orders/{workOrderId}/labors/{laborItemId}/remove", WORK_ORDER_ID, LABOR_ITEM_TO_REMOVE_ID)
                .then()
                .statusCode(HttpStatus.NO_CONTENT.value());

        assertTrue(workOrderLaborItemRepository.findById(LABOR_ITEM_TO_REMOVE_ID).isEmpty());
    }


    @Test
    @Sql(scripts = "/sql/work_order_add_labor_test_data.sql")
    @DisplayName("Deve adicionar mão de obra a uma ordem de serviço")
    void shouldAddLaborToWorkOrder() {
        RestAssuredMockMvc.given()
                .header("Authorization", "Bearer " + authToken)
                .contentType(MediaType.APPLICATION_JSON)
                .when()
                .post("/work-orders/{workOrderId}/labors/{laborId}/add", ADD_LABOR_WORK_ORDER_ID, ADD_LABOR_ID)
                .then()
                .statusCode(HttpStatus.NO_CONTENT.value());

        WorkOrder workOrder = workOrderRepository.findById(ADD_LABOR_WORK_ORDER_ID).orElseThrow();
        List<WorkOrderLaborItem> laborItems = new ArrayList<>(workOrder.getLaborItems());
        assertEquals(1, laborItems.size());
        assertEquals(ADD_LABOR_ID, laborItems.get(0).getLaborId());
    }
}
