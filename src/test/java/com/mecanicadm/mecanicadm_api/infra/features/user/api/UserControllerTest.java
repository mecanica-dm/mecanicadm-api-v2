package com.mecanicadm.mecanicadm_api.infra.features.user.api;

import com.mecanicadm.mecanicadm_api.core.user.domain.User;
import com.mecanicadm.mecanicadm_api.core.user.usecase.*;
import com.mecanicadm.mecanicadm_api.core.user.usecase.command.SoftDeleteUserCommand;
import com.mecanicadm.mecanicadm_api.core.user.usecase.dto.AuthenticateUserResponse;
import com.mecanicadm.mecanicadm_api.infra.features.user.api.dto.*;
import com.mecanicadm.mecanicadm_api.infra.security.UserAdapter;
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
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest extends AbstractIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CreateUserUseCase createUserUseCase;

    @MockitoBean
    private UpdateUserUseCase updateUserUseCase;

    @MockitoBean
    private AuthenticateUserUseCase authenticateUserUseCase;

    @MockitoBean
    private SoftDeleteUserUseCase softDeleteUserUseCase;

    @MockitoBean
    private GetUserByIdUseCase getUserByIdUseCase;

    @MockitoBean
    private RequestPasswordResetUseCase requestPasswordResetUseCase;

    @MockitoBean
    private ResetPasswordUseCase resetPasswordUseCase;

    @BeforeEach
    void setUp() {
        RestAssuredMockMvc.mockMvc(mockMvc);
    }

    @Test
    @WithMockUser
    @DisplayName("Deve fazer login e retornar 200 OK")
    void shouldLoginAndReturn200() {
        UUID userId = UUID.randomUUID();
        var authResponse = new AuthenticateUserResponse("token123", userId, "João", "joao@email.com");
        when(authenticateUserUseCase.execute(any())).thenReturn(authResponse);

        LoginRequest request = new LoginRequest("joao@email.com", "123456");

        RestAssuredMockMvc.given()
                .postProcessors(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .body(request)
                .when()
                .post("/user/login")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("access_token", equalTo("token123"))
                .body("name", equalTo("João"))
                .body("email", equalTo("joao@email.com"));
    }

    @Test
    @WithMockUser
    @DisplayName("Deve retornar 400 ao fazer login com dados invalidos")
    void shouldReturn400WhenLoginWithInvalidData() {
        LoginRequest invalidRequest = new LoginRequest("", "");

        RestAssuredMockMvc.given()
                .postProcessors(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .body(invalidRequest)
                .when()
                .post("/user/login")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @WithMockUser
    @DisplayName("Deve criar usuario e retornar 201 Created")
    void shouldCreateUserAndReturn201() {
        UUID userId = UUID.randomUUID();
        when(createUserUseCase.execute(any())).thenReturn(userId);

        CreateUserRequest request = new CreateUserRequest("joao@email.com", "123456", "João");

        RestAssuredMockMvc.given()
                .postProcessors(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .body(request)
                .when()
                .post("/user")
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .body(equalTo("\"" + userId.toString() + "\""));
    }

    @Test
    @WithMockUser
    @DisplayName("Deve retornar 400 ao criar usuario com dados invalidos")
    void shouldReturn400WhenCreateUserWithInvalidData() {
        CreateUserRequest invalidRequest = new CreateUserRequest("", "", "");

        RestAssuredMockMvc.given()
                .postProcessors(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .body(invalidRequest)
                .when()
                .post("/user")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @WithMockUser
    @DisplayName("Deve buscar usuario por ID e retornar 200 OK")
    void shouldFindUserByIdAndReturn200() {
        UUID userId = UUID.randomUUID();
        User user = mock(User.class);
        when(user.getEmail()).thenReturn("joao@email.com");
        when(user.getName()).thenReturn("João");
        when(getUserByIdUseCase.execute(any())).thenReturn(user);

        RestAssuredMockMvc.given()
                .when()
                .get("/user/{id}", userId)
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("email", equalTo("joao@email.com"))
                .body("name", equalTo("João"));
    }

    @Test
    @WithMockUser
    @DisplayName("Deve solicitar reset de senha e retornar 200 OK")
    void shouldRequestPasswordResetAndReturn200() {
        ForgotPasswordRequest request = new ForgotPasswordRequest("joao@email.com");

        RestAssuredMockMvc.given()
                .postProcessors(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .body(request)
                .when()
                .post("/user/forgot-password")
                .then()
                .statusCode(HttpStatus.OK.value());

        verify(requestPasswordResetUseCase, times(1)).execute(any());
    }

    @Test
    @WithMockUser
    @DisplayName("Deve resetar senha e retornar 200 OK")
    void shouldResetPasswordAndReturn200() {
        ResetPasswordRequest request = new ResetPasswordRequest("token123", "novaSenha123");

        RestAssuredMockMvc.given()
                .postProcessors(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .body(request)
                .when()
                .post("/user/reset-password")
                .then()
                .statusCode(HttpStatus.OK.value());

        verify(resetPasswordUseCase, times(1)).execute(any());
    }

    @Test
    @DisplayName("Deve atualizar usuario e retornar 200 OK")
    void shouldUpdateUserAndReturn200() {
        UserAdapter userAdapter = createUserAdapter();
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(userAdapter, null, userAdapter.getAuthorities()));

        UpdateUserRequest request = new UpdateUserRequest("João Atualizado", null, null);

        RestAssuredMockMvc.given()
                .postProcessors(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .body(request)
                .when()
                .put("/user")
                .then()
                .statusCode(HttpStatus.OK.value());

        verify(updateUserUseCase, times(1)).execute(any());
    }

    @Test
    @DisplayName("Deve deletar usuario e retornar 204 No Content")
    void shouldDeleteUserAndReturn204() {
        UserAdapter userAdapter = createUserAdapter();
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(userAdapter, null, userAdapter.getAuthorities()));

        RestAssuredMockMvc.given()
                .postProcessors(csrf())
                .when()
                .delete("/user")
                .then()
                .statusCode(HttpStatus.NO_CONTENT.value());

        verify(softDeleteUserUseCase, times(1)).execute(any(SoftDeleteUserCommand.class));
    }

    private UserAdapter createUserAdapter() {
        User user = mock(User.class);
        when(user.getId()).thenReturn(UUID.randomUUID());
        when(user.isDeleted()).thenReturn(false);
        return new UserAdapter(user);
    }
}
