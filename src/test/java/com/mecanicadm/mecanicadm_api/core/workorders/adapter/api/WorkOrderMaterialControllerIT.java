package com.mecanicadm.mecanicadm_api.core.workorders.adapter.api;

import com.mecanicadm.mecanicadm_api.core.workorders.domain.WorkOrder;
import com.mecanicadm.mecanicadm_api.core.workorders.domain.WorkOrderMaterialItem;
import com.mecanicadm.mecanicadm_api.testutils.AbstractIntegrationTest;
import com.mecanicadm.mecanicadm_api.core.workorders.usecase.command.AddMaterialToWorkOrderCommand;
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
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.mecanicadm.mecanicadm_api.testutils.AuthUtils.getAuthToken;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class WorkOrderMaterialControllerIT extends AbstractIntegrationTest {

    private static final UUID ADD_MATERIAL_WORK_ORDER_ID = UUID.fromString("660e8400-e29b-41d4-a716-446655440096");
    private static final UUID ADD_MATERIAL_ID = UUID.fromString("660e8400-e29b-41d4-a716-446655440097");

    private static final UUID REMOVE_MATERIAL_WORK_ORDER_ID = UUID.fromString("660e8400-e29b-41d4-a716-446655440412");
    private static final UUID REMOVE_MATERIAL_ITEM_ID = UUID.fromString("660e8400-e29b-41d4-a716-446655440097");

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
    @Sql(scripts = "/sql/work_order_add_material_test_data.sql")
    @DisplayName("Deve adicionar material a uma ordem de serviço")
    void shouldAddMaterialToWorkOrder() {
        AddMaterialToWorkOrderCommand command = new AddMaterialToWorkOrderCommand(
                null,
                null,
                2
        );

        RestAssuredMockMvc.given()
                .header("Authorization", "Bearer " + authToken)
                .contentType(MediaType.APPLICATION_JSON)
                .body(command)
                .when()
                .post("/work-orders/{workOrderId}/materials/{materialId}/add", ADD_MATERIAL_WORK_ORDER_ID, ADD_MATERIAL_ID)
                .then()
                .statusCode(HttpStatus.NO_CONTENT.value());

        WorkOrder workOrder = workOrderRepository.findById(ADD_MATERIAL_WORK_ORDER_ID).orElseThrow();
        List<WorkOrderMaterialItem> materialItems = new ArrayList<>(workOrder.getMaterialItems());
        assertEquals(1, materialItems.size());
        assertEquals(ADD_MATERIAL_ID, materialItems.get(0).getMaterialId());
        assertEquals(2, materialItems.get(0).getQuantity());
    }

    @Test
    @Sql(scripts = "/sql/work_order_remove_material_test_data.sql")
    @DisplayName("Deve remover material de uma ordem de serviço")
    void shouldRemoveMaterialFromWorkOrder() {

        RestAssuredMockMvc.given()
                .header("Authorization", "Bearer " + authToken)
                .when()
                .post("/work-orders/{workOrderId}/materials/{materialId}/remove", REMOVE_MATERIAL_WORK_ORDER_ID, REMOVE_MATERIAL_ITEM_ID)
                .then()
                .statusCode(HttpStatus.NO_CONTENT.value());

        WorkOrder workOrder = workOrderRepository.findById(REMOVE_MATERIAL_WORK_ORDER_ID).orElseThrow();
        assertTrue(workOrder.getMaterialItems().isEmpty());
    }
}