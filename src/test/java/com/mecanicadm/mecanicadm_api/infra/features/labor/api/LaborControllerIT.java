package com.mecanicadm.mecanicadm_api.infra.features.labor.api;

import com.mecanicadm.mecanicadm_api.core.labor.usecase.command.CreateLaborCommand;
import com.mecanicadm.mecanicadm_api.core.labor.usecase.command.UpdateLaborCommand;
import com.mecanicadm.mecanicadm_api.testutils.AbstractIntegrationTest;
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

import java.math.BigDecimal;
import java.util.UUID;

import static com.mecanicadm.mecanicadm_api.testutils.AuthUtils.getAuthToken;
import static org.hamcrest.Matchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class LaborControllerIT extends AbstractIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    private String token;

    @BeforeEach
    void setUp() {
        RestAssuredMockMvc.mockMvc(mockMvc);
        token = getAuthToken("admin@example.com", "Admin User");
    }

    @Test
    @DisplayName("Deve criar um novo serviço com sucesso")
    void shouldCreateLabor() {
        CreateLaborCommand command = new CreateLaborCommand("Alinhamento", new BigDecimal("100.00"));

        RestAssuredMockMvc.given()
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .body(command)
                .when()
                .post("/labor")
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .header("Location", not(emptyString()))
                .body(notNullValue());
    }

    @Test
    @Sql(scripts = "/sql/labor.sql")
    @DisplayName("Deve listar serviços com filtros e paginação")
    void shouldListLaborsWithFilters() {
        RestAssuredMockMvc.given()
                .header("Authorization", "Bearer " + token)
                .queryParam("name", "Óleo")
                .queryParam("page", 0)
                .queryParam("size", 10)
                .when()
                .get("/labor")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("content", hasSize(1))
                .body("content[0].name", equalTo("Troca de Óleo"));
    }

    @Test
    @Sql(scripts = "/sql/labor.sql")
    @DisplayName("Deve buscar um serviço por ID")
    void shouldFindLaborById() {
        UUID laborId = UUID.fromString("550e8400-e29b-41d4-a716-446655440011");

        RestAssuredMockMvc.given()
                .header("Authorization", "Bearer " + token)
                .when()
                .get("/labor/{id}", laborId)
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("id", equalTo(laborId.toString()))
                .body("name", equalTo("Troca de Óleo"));
    }

    @Test
    @Sql(scripts = "/sql/labor.sql")
    @DisplayName("Deve atualizar um serviço existente")
    void shouldUpdateLabor() {
        UUID laborId = UUID.fromString("550e8400-e29b-41d4-a716-446655440012");
        UpdateLaborCommand command = new UpdateLaborCommand(laborId, "Revisão Premium", new BigDecimal("500.00"));

        RestAssuredMockMvc.given()
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .body(command)
                .when()
                .put("/labor/{id}", laborId)
                .then()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }

    @Test
    @Sql(scripts = "/sql/labor.sql")
    @DisplayName("Deve excluir um serviço")
    void shouldDeleteLabor() {
        UUID laborId = UUID.fromString("550e8400-e29b-41d4-a716-446655440013");

        RestAssuredMockMvc.given()
                .header("Authorization", "Bearer " + token)
                .when()
                .delete("/labor/{id}", laborId)
                .then()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }
}
