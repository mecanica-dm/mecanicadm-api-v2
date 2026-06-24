package com.mecanicadm.mecanicadm_api.infra.features.client.api;

import com.mecanicadm.mecanicadm_api.core.client.domain.Client;
import com.mecanicadm.mecanicadm_api.core.client.domain.port.ClientPageResult;
import com.mecanicadm.mecanicadm_api.core.client.usecase.CreateClientUseCase;
import com.mecanicadm.mecanicadm_api.core.client.usecase.GetAllClientUseCase;
import com.mecanicadm.mecanicadm_api.core.client.usecase.SoftDeleteClientUseCase;
import com.mecanicadm.mecanicadm_api.core.client.usecase.UpdateClientUseCase;
import com.mecanicadm.mecanicadm_api.core.client.usecase.command.SoftDeleteClientCommand;
import com.mecanicadm.mecanicadm_api.infra.features.client.api.dto.request.CreateClientRequest;
import com.mecanicadm.mecanicadm_api.infra.features.client.api.dto.request.UpdateClientRequest;
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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@SpringBootTest
@AutoConfigureMockMvc
class ClientControllerTest extends AbstractIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CreateClientUseCase createClientUseCase;

    @MockitoBean
    private GetAllClientUseCase getAllClientUseCase;

    @MockitoBean
    private UpdateClientUseCase updateClientUseCase;

    @MockitoBean
    private SoftDeleteClientUseCase softDeleteClientUseCase;

    @BeforeEach
    void setUp() {
        RestAssuredMockMvc.mockMvc(mockMvc);
    }

    @Test
    @WithMockUser
    @DisplayName("Deve criar cliente e retornar 201 Created")
    void shouldCreateClientAndReturn201() {
        UUID clientId = UUID.randomUUID();
        when(createClientUseCase.execute(any())).thenReturn(clientId);

        CreateClientRequest request = new CreateClientRequest("João", "joao@email.com", "12345678909", "11999999999");

        RestAssuredMockMvc.given()
                .postProcessors(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .body(request)
                .when()
                .post("/clients")
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .body(equalTo("\"" + clientId.toString() + "\""));
    }

    @Test
    @WithMockUser
    @DisplayName("Deve retornar 400 ao criar cliente com dados invalidos")
    void shouldReturn400WhenCreateCommandIsInvalid() {
        CreateClientRequest invalidRequest = new CreateClientRequest("", "", "", "");

        RestAssuredMockMvc.given()
                .postProcessors(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .body(invalidRequest)
                .when()
                .post("/clients")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @WithMockUser
    @DisplayName("Deve atualizar cliente e retornar 200 OK")
    void shouldUpdateClientAndReturn200() {
        UUID clientId = UUID.randomUUID();
        UpdateClientRequest request = new UpdateClientRequest(clientId, "João Atualizado", "joao@email.com", "12345678909", "11999999999");

        RestAssuredMockMvc.given()
                .postProcessors(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .body(request)
                .when()
                .put("/clients/{id}", clientId)
                .then()
                .statusCode(HttpStatus.OK.value());

        verify(updateClientUseCase, times(1)).execute(any());
    }

    @Test
    @WithMockUser
    @DisplayName("Deve deletar cliente e retornar 204 No Content")
    void shouldDeleteClientAndReturn204() {
        UUID clientId = UUID.randomUUID();

        RestAssuredMockMvc.given()
                .postProcessors(csrf())
                .when()
                .delete("/clients/{id}", clientId)
                .then()
                .statusCode(HttpStatus.NO_CONTENT.value());

        verify(softDeleteClientUseCase, times(1)).execute(any(SoftDeleteClientCommand.class));
    }

    @Test
    @WithMockUser
    @DisplayName("Deve listar clientes e retornar 200 OK")
    void shouldGetAllClientsAndReturn200() {
        Client client = mock(Client.class);
        when(client.getId()).thenReturn(UUID.randomUUID());
        when(client.getName()).thenReturn("João");
        when(client.getEmail()).thenReturn("joao@email.com");
        when(client.getDocument()).thenReturn("12345678909");
        when(client.getPhone()).thenReturn("11999999999");

        var pageResult = new ClientPageResult(List.of(client), 1L);
        when(getAllClientUseCase.execute(any())).thenReturn(pageResult);

        RestAssuredMockMvc.given()
                .when()
                .get("/clients")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("content[0].name", equalTo("João"))
                .body("page.totalElements", equalTo(1));
    }
}
