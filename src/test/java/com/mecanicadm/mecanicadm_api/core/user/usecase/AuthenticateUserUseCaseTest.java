package com.mecanicadm.mecanicadm_api.core.user.usecase;

import com.mecanicadm.mecanicadm_api.infra.features.user.api.dto.AuthenticationResponse;
import com.mecanicadm.mecanicadm_api.core.user.domain.User;
import com.mecanicadm.mecanicadm_api.core.user.domain.port.UserGateway;
import com.mecanicadm.mecanicadm_api.core.user.exception.UserExceptions;
import com.mecanicadm.mecanicadm_api.core.user.usecase.query.AuthenticateUserQuery;
import com.mecanicadm.mecanicadm_api.infra.services.TokenService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthenticateUserUseCaseTest {

    @Mock
    private UserGateway userGateway;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private TokenService tokenService;

    private AuthenticateUserUseCase authenticateUserUseCase;

    @BeforeEach
    void setUp() {
        authenticateUserUseCase = new AuthenticateUserUseCase(userGateway, passwordEncoder, tokenService);
    }

    @Test
    @DisplayName("Deve autenticar o usuário com sucesso quando as credenciais estão corretas")
    void shouldAuthenticateUserSuccessfully() {
        AuthenticateUserQuery query = new AuthenticateUserQuery("test@example.com", "password123");
        User user = mock(User.class);
        UUID userId = UUID.randomUUID();
        String token = "jwt-token";

        when(userGateway.findByEmail(query.email())).thenReturn(Optional.of(user));
        doNothing().when(user).verifyPassword(query.password(), passwordEncoder);
        when(user.getEmail()).thenReturn(query.email());
        when(user.getId()).thenReturn(userId);
        when(user.getName()).thenReturn("Test User");
        when(tokenService.generateToken(query.email())).thenReturn(token);

        AuthenticationResponse response = authenticateUserUseCase.execute(query);

        assertNotNull(response);
        assertEquals(token, response.access_token());
        assertEquals(userId.toString(), response.userId());
        assertEquals("Test User", response.name());
        assertEquals(query.email(), response.email());

        verify(userGateway).findByEmail(query.email());
        verify(user).verifyPassword(query.password(), passwordEncoder);
        verify(tokenService).generateToken(query.email());
    }

    @Test
    @DisplayName("Deve lançar exceção quando o e-mail não é encontrado")
    void shouldThrowExceptionWhenUserNotFound() {
        AuthenticateUserQuery query = new AuthenticateUserQuery("test@example.com", "password123");
        when(userGateway.findByEmail(query.email())).thenReturn(Optional.empty());

        assertThrows(UserExceptions.BadCredentials.class, () -> authenticateUserUseCase.execute(query));

        verify(userGateway).findByEmail(query.email());
        verifyNoInteractions(tokenService);
    }

    @Test
    @DisplayName("Deve propagar a exceção de BadCredentials quando a senha estiver incorreta")
    void shouldPropagateBadCredentialsWhenPasswordIsIncorrect() {
        AuthenticateUserQuery query = new AuthenticateUserQuery("test@example.com", "wrong-password");
        User user = mock(User.class);

        when(userGateway.findByEmail(query.email())).thenReturn(Optional.of(user));
        doThrow(new UserExceptions.BadCredentials()).when(user).verifyPassword(query.password(), passwordEncoder);

        assertThrows(UserExceptions.BadCredentials.class, () -> authenticateUserUseCase.execute(query));

        verify(userGateway).findByEmail(query.email());
        verify(user).verifyPassword(query.password(), passwordEncoder);
        verifyNoInteractions(tokenService);
    }
}
