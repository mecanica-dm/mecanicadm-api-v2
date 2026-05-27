package com.mecanicadm.mecanicadm_api.core.client.adapter.api;

import com.mecanicadm.mecanicadm_api.core.client.usecase.command.CreateClientCommand;
import com.mecanicadm.mecanicadm_api.core.client.usecase.command.UpdateClientCommand;
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
import static org.hamcrest.Matchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class ClientControllerIT {

    @Autowired
    private MockMvc mockMvc;

    private String token;

    @BeforeEach
    void setUp() {
        RestAssuredMockMvc.mockMvc(mockMvc);
        token = getAuthToken("admin@example.com", "Admin User");
    }

    @Test
    @DisplayName("Deve criar um novo cliente com sucesso")
    void shouldCreateClient() {
        CreateClientCommand command = new CreateClientCommand(
                "Novo Cliente Teste",
                "novo.cliente@example.com",
                "15831965015",
                "48999999999"
        );

        RestAssuredMockMvc.given()
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .body(command)
                .when()
                .post("/clients")
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .header("Location", not(emptyString()));
    }

    @Test
    @Sql(scripts = "/sql/client.sql")
    @DisplayName("Deve listar clientes com filtros e paginação")
    void shouldListClientsWithFilters() {
        RestAssuredMockMvc.given()
                .header("Authorization", "Bearer " + token)
                .queryParam("name", "João")
                .queryParam("page", 0)
                .queryParam("size", 10)
                .when()
                .get("/clients")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("content", hasSize(1))
                .body("content[0].name", equalTo("João Silva"));
    }

    @Test
    @Sql(scripts = "/sql/client.sql")
    @DisplayName("Deve atualizar um cliente existente")
    void shouldUpdateClient() {
        UUID clientId = UUID.fromString("550e8400-e29b-41d4-a716-446655440001");
        UpdateClientCommand command = new UpdateClientCommand(
                null,
                "João Silva Atualizado",
                "joao.atualizado@example.com",
                "65503488032",
                "88999878986"
        );

        RestAssuredMockMvc.given()
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .body(command)
                .when()
                .put("/clients/{id}", clientId)
                .then()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    @Sql(scripts = "/sql/client.sql")
    @DisplayName("Deve realizar exclusão lógica (soft delete) do cliente")
    void shouldSoftDeleteClient() {
        UUID clientId = UUID.fromString("550e8400-e29b-41d4-a716-446655440003");

        RestAssuredMockMvc.given()
                .header("Authorization", "Bearer " + token)
                .when()
                .delete("/clients/{id}", clientId)
                .then()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }

    @Test
    @DisplayName("Deve retornar 400 ao tentar criar cliente com Documento inválido")
    void shouldReturn400WhenDocumentIsInvalid() {
        CreateClientCommand command = new CreateClientCommand(
                "Cliente Documento Invalido",
                "doc.invalido@example.com",
                "invalid_doc",
                "48999999999"
        );

        RestAssuredMockMvc.given()
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .body(command)
                .when()
                .post("/clients")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }
}
