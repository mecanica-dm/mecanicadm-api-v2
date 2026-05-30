package com.mecanicadm.mecanicadm_api.core.vehicle.adapter.api;

import com.mecanicadm.mecanicadm_api.core.vehicle.domain.Vehicle;
import com.mecanicadm.mecanicadm_api.core.vehicle.usecase.CreateVehicleUseCase;
import com.mecanicadm.mecanicadm_api.core.vehicle.usecase.DeleteVehicleUseCase;
import com.mecanicadm.mecanicadm_api.core.vehicle.usecase.GetVehicleByIdUseCase;
import com.mecanicadm.mecanicadm_api.core.vehicle.usecase.UpdateVehicleUseCase;
import com.mecanicadm.mecanicadm_api.core.vehicle.usecase.command.DeleteVehicleCommand;
import com.mecanicadm.mecanicadm_api.core.vehicle.usecase.query.GetVehicleByIdQuery;
import com.mecanicadm.mecanicadm_api.infra.features.vehicle.api.dto.request.CreateVehicleRequest;
import com.mecanicadm.mecanicadm_api.infra.features.vehicle.api.dto.request.UpdateVehicleRequest;
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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class VehicleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CreateVehicleUseCase createVehicleUseCase;

    @MockitoBean
    private GetVehicleByIdUseCase getVehicleByIdUseCase;

    @MockitoBean
    private UpdateVehicleUseCase updateVehicleUseCase;

    @MockitoBean
    private DeleteVehicleUseCase deleteVehicleUseCase;

    @BeforeEach
    void setUp() {
        RestAssuredMockMvc.mockMvc(mockMvc);
    }

    @Test
    @WithMockUser
    @DisplayName("Deve criar um veículo e retornar 201 Created")
    void shouldCreateVehicleAndReturn201() {
        CreateVehicleRequest request = new CreateVehicleRequest("Civic", "ABC1234", "Honda", Short.valueOf("2023"));
        when(createVehicleUseCase.execute(any())).thenReturn("ABC1234");

        RestAssuredMockMvc.given()
                .postProcessors(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .body(request)
                .when()
                .post("/vehicle")
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .header("Location", equalTo("/vehicle/ABC1234"))
                .body(equalTo("ABC1234"));
    }

    @Test
    @WithMockUser
    @DisplayName("Deve retornar 400 Bad Request ao tentar criar veículo com dados inválidos")
    void shouldReturn400WhenCommandIsInvalid() {
        CreateVehicleRequest invalidCommand = new CreateVehicleRequest("", "", "", null);

        RestAssuredMockMvc.given()
                .postProcessors(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .body(invalidCommand)
                .when()
                .post("/vehicle")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @WithMockUser
    @DisplayName("Deve atualizar um veículo e retornar 204 No Content")
    void shouldUpdateVehicleAndReturn200() {
        String licensePlate = "ABC1234";
        UpdateVehicleRequest command = new UpdateVehicleRequest("Civic Updated", "Honda", Short.valueOf("2019"));
        when(updateVehicleUseCase.execute(any())).thenReturn(new Vehicle(command.model(), licensePlate, command.brand(), command.modelYear()));

        RestAssuredMockMvc.given()
                .postProcessors(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .body(command)
                .when()
                .put("/vehicle/{licensePlate}", licensePlate)
                .then()
                .statusCode(HttpStatus.NO_CONTENT.value());

        verify(updateVehicleUseCase, times(1)).execute(any());
    }

    @Test
    @WithMockUser
    @DisplayName("Deve retornar 400 Bad Request ao tentar atualizar veículo com dados inválidos")
    void shouldReturn400WhenUpdateCommandIsInvalid() {
        String licensePlate = "ABC1234";
        UpdateVehicleRequest invalidCommand = new UpdateVehicleRequest("", "", null);

        RestAssuredMockMvc.given()
                .postProcessors(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .body(invalidCommand)
                .when()
                .put("/vehicle/{licensePlate}", licensePlate)
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @WithMockUser
    @DisplayName("Deve excluir um veículo e retornar 204 No Content")
    void shouldDeleteVehicleAndReturn204() {
        String licensePlate = "ABC1234";

        RestAssuredMockMvc.given()
                .postProcessors(csrf())
                .when()
                .delete("/vehicle/{licensePlate}", licensePlate)
                .then()
                .statusCode(HttpStatus.NO_CONTENT.value());

        verify(deleteVehicleUseCase, times(1)).execute(any(DeleteVehicleCommand.class));
    }

    @Test
    @WithMockUser
    @DisplayName("Deve buscar um veículo e retornar 200 OK")
    void shouldFindVehicleAndReturn200() {
        String licensePlate = "ABC1234";
        var vehicleResponse = new Vehicle("Civic", licensePlate, "Honda", Short.valueOf("2023"));
        when(getVehicleByIdUseCase.execute(new GetVehicleByIdQuery(licensePlate))).thenReturn(vehicleResponse);

        RestAssuredMockMvc.given()
                .when()
                .get("/vehicle/{licensePlate}", licensePlate)
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("licensePlate", equalTo(licensePlate))
                .body("model", equalTo("Civic"));
    }
}
