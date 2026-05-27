package com.mecanicadm.mecanicadm_api.core.vehicle.adapter.api;

import com.mecanicadm.mecanicadm_api.core.vehicle.usecase.command.CreateVehicleCommand;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static com.mecanicadm.mecanicadm_api.testutils.AuthUtils.getAuthToken;
import static org.hamcrest.Matchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class VehicleControllerIT {

    @Autowired
    private MockMvc mockMvc;

    private String authToken;

    @BeforeEach
    void setUp() {
        RestAssuredMockMvc.mockMvc(mockMvc);
        authToken = getAuthToken("vehicle-admin@example.com", "password123", "Vehicle Admin");
    }

    @Test
    @DisplayName("Deve filtrar veículos por placa e modelo")
    void shouldFilterVehicles() {
        CreateVehicleCommand v1 = new CreateVehicleCommand("Civic", "MJE-0545", "Honda", Short.valueOf("2023"));
        CreateVehicleCommand v2 = new CreateVehicleCommand("Corolla", "JYL-8719", "Toyota", Short.valueOf("2022"));
        CreateVehicleCommand v3 = new CreateVehicleCommand("Civic Si", "MYP-7853", "Honda", Short.valueOf("2024"));

        RestAssuredMockMvc.given().header("Authorization", "Bearer " + authToken).contentType(MediaType.APPLICATION_JSON).body(v1).post("/vehicle");
        RestAssuredMockMvc.given().header("Authorization", "Bearer " + authToken).contentType(MediaType.APPLICATION_JSON).body(v2).post("/vehicle");
        RestAssuredMockMvc.given().header("Authorization", "Bearer " + authToken).contentType(MediaType.APPLICATION_JSON).body(v3).post("/vehicle");

        RestAssuredMockMvc.given()
                .header("Authorization", "Bearer " + authToken)
                .queryParam("model", "Civic")
                .when()
                .get("/vehicle")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("content.size()", greaterThanOrEqualTo(2))
                .body("content.model", hasItems("Civic", "Civic Si"));

        RestAssuredMockMvc.given()
                .header("Authorization", "Bearer " + authToken)
                .queryParam("licensePlate", "MJE-0545")
                .when()
                .get("/vehicle")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("content.size()", equalTo(1))
                .body("content[0].model", equalTo("Civic"));
    }
}
