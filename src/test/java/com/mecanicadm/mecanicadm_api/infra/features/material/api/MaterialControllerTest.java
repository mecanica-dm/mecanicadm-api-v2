package com.mecanicadm.mecanicadm_api.infra.features.material.api;

import com.mecanicadm.mecanicadm_api.core.material.domain.Material;
import com.mecanicadm.mecanicadm_api.core.material.domain.enums.MaterialType;
import com.mecanicadm.mecanicadm_api.core.material.domain.port.MaterialPageResult;
import com.mecanicadm.mecanicadm_api.core.material.usecase.CreateMaterialUseCase;
import com.mecanicadm.mecanicadm_api.core.material.usecase.GetAllMaterialsUseCase;
import com.mecanicadm.mecanicadm_api.core.material.usecase.GetMaterialByIdUseCase;
import com.mecanicadm.mecanicadm_api.core.material.usecase.SoftDeleteMaterialUseCase;
import com.mecanicadm.mecanicadm_api.core.material.usecase.UpdateMaterialUseCase;
import com.mecanicadm.mecanicadm_api.core.material.usecase.command.SoftDeleteMaterialCommand;
import com.mecanicadm.mecanicadm_api.infra.features.material.api.dto.request.CreateMaterialRequest;
import com.mecanicadm.mecanicadm_api.infra.features.material.api.dto.request.UpdateMaterialRequest;
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

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@SpringBootTest
@AutoConfigureMockMvc
class MaterialControllerTest extends AbstractIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CreateMaterialUseCase createMaterialUseCase;

    @MockitoBean
    private UpdateMaterialUseCase updateMaterialUseCase;

    @MockitoBean
    private GetMaterialByIdUseCase getMaterialByIdUseCase;

    @MockitoBean
    private GetAllMaterialsUseCase getAllMaterialsUseCase;

    @MockitoBean
    private SoftDeleteMaterialUseCase softDeleteMaterialUseCase;

    @BeforeEach
    void setUp() {
        RestAssuredMockMvc.mockMvc(mockMvc);
    }

    @Test
    @WithMockUser
    @DisplayName("Deve criar material e retornar 201 Created")
    void shouldCreateMaterialAndReturn201() {
        UUID materialId = UUID.randomUUID();
        when(createMaterialUseCase.execute(any())).thenReturn(materialId);

        CreateMaterialRequest request = new CreateMaterialRequest("Óleo 5W30", "Shell", "Óleo sintético", BigDecimal.valueOf(50), MaterialType.PART, 10);

        RestAssuredMockMvc.given()
                .postProcessors(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .body(request)
                .when()
                .post("/materials")
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .body(equalTo("\"" + materialId.toString() + "\""));
    }

    @Test
    @WithMockUser
    @DisplayName("Deve retornar 400 ao criar material com dados invalidos")
    void shouldReturn400WhenCreateCommandIsInvalid() {
        CreateMaterialRequest invalidRequest = new CreateMaterialRequest(null, null, null, null, null, 0);

        RestAssuredMockMvc.given()
                .postProcessors(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .body(invalidRequest)
                .when()
                .post("/materials")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @WithMockUser
    @DisplayName("Deve atualizar material e retornar 200 OK")
    void shouldUpdateMaterialAndReturn200() {
        UUID materialId = UUID.randomUUID();
        UpdateMaterialRequest request = new UpdateMaterialRequest(materialId, "Óleo 5W30", "Shell", "Óleo sintético", BigDecimal.valueOf(55), MaterialType.PART);

        RestAssuredMockMvc.given()
                .postProcessors(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .body(request)
                .when()
                .put("/materials/{id}", materialId)
                .then()
                .statusCode(HttpStatus.OK.value());

        verify(updateMaterialUseCase, times(1)).execute(any());
    }

    @Test
    @WithMockUser
    @DisplayName("Deve deletar material e retornar 204 No Content")
    void shouldDeleteMaterialAndReturn204() {
        UUID materialId = UUID.randomUUID();

        RestAssuredMockMvc.given()
                .postProcessors(csrf())
                .when()
                .delete("/materials/{id}", materialId)
                .then()
                .statusCode(HttpStatus.NO_CONTENT.value());

        verify(softDeleteMaterialUseCase, times(1)).execute(any(SoftDeleteMaterialCommand.class));
    }

    @Test
    @WithMockUser
    @DisplayName("Deve buscar material por ID e retornar 200 OK")
    void shouldFindMaterialByIdAndReturn200() {
        UUID materialId = UUID.randomUUID();
        Material material = mock(Material.class);
        when(material.getId()).thenReturn(materialId);
        when(material.getName()).thenReturn("Óleo 5W30");
        when(material.getBrand()).thenReturn("Shell");
        when(material.getPrice()).thenReturn(BigDecimal.valueOf(50));
        when(material.getType()).thenReturn(MaterialType.PART);
        when(getMaterialByIdUseCase.execute(any())).thenReturn(material);

        RestAssuredMockMvc.given()
                .when()
                .get("/materials/{id}", materialId)
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("name", equalTo("Óleo 5W30"));
    }

    @Test
    @WithMockUser
    @DisplayName("Deve listar materiais e retornar 200 OK")
    void shouldGetAllMaterialsAndReturn200() {
        Material material = mock(Material.class);
        when(material.getId()).thenReturn(UUID.randomUUID());
        when(material.getName()).thenReturn("Óleo 5W30");
        when(material.getBrand()).thenReturn("Shell");
        when(material.getPrice()).thenReturn(BigDecimal.valueOf(50));
        when(material.getType()).thenReturn(MaterialType.PART);

        var pageResult = new MaterialPageResult(List.of(material), 1L);
        when(getAllMaterialsUseCase.execute(any())).thenReturn(pageResult);

        RestAssuredMockMvc.given()
                .when()
                .get("/materials")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("content[0].name", equalTo("Óleo 5W30"))
                .body("page.totalElements", equalTo(1));
    }
}
