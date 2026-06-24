package com.mecanicadm.mecanicadm_api.infra.features.stockmovements.api;

import com.mecanicadm.mecanicadm_api.core.stockmovements.domain.StockMovements;
import com.mecanicadm.mecanicadm_api.core.stockmovements.domain.StockStatement;
import com.mecanicadm.mecanicadm_api.core.stockmovements.domain.enums.MovementType;
import com.mecanicadm.mecanicadm_api.core.stockmovements.usecase.GetStockStatementUseCase;
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

import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc
class StockMovementsControllerTest extends AbstractIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private GetStockStatementUseCase getStockStatementUseCase;

    @BeforeEach
    void setUp() {
        RestAssuredMockMvc.mockMvc(mockMvc);
    }

    @Test
    @WithMockUser
    @DisplayName("Deve buscar statement do stock e retornar 200 OK")
    void shouldGetStatementAndReturn200() {
        UUID materialId = UUID.randomUUID();
        StockMovements movement = mock(StockMovements.class);
        when(movement.getId()).thenReturn(UUID.randomUUID());
        when(movement.getQuantity()).thenReturn(5);
        when(movement.getType()).thenReturn(MovementType.ADDITION);

        StockStatement statement = mock(StockStatement.class);
        when(statement.materialId()).thenReturn(materialId);
        when(statement.currentBalance()).thenReturn(10);
        when(statement.movements()).thenReturn(List.of(movement));

        when(getStockStatementUseCase.execute(any())).thenReturn(statement);

        RestAssuredMockMvc.given()
                .when()
                .get("/stock-movements/{materialId}/statement", materialId)
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("materialId", equalTo(materialId.toString()))
                .body("currentBalance", equalTo(10));
    }
}
