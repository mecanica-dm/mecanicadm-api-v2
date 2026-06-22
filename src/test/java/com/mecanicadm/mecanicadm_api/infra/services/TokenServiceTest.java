package com.mecanicadm.mecanicadm_api.infra.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.mecanicadm.mecanicadm_api.infra.security.exception.InvalidTokenException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mockStatic;

class TokenServiceTest {

    private TokenServiceImpl tokenService;

    @BeforeEach
    void setUp() {
        tokenService = new TokenServiceImpl();
        ReflectionTestUtils.setField(tokenService, "secret", "test-secret");
    }

    @Test
    @DisplayName("Deve gerar um token válido para um e-mail")
    void shouldGenerateToken() {
        String email = "user@test.com";
        String token = tokenService.generateToken(email);

        assertNotNull(token);
        assertFalse(token.isEmpty());

        DecodedJWT decodedJWT = JWT.decode(token);
        assertEquals(email, decodedJWT.getSubject());
        assertEquals("mecanicadm_api", decodedJWT.getIssuer());
    }

    @Test
    @DisplayName("Deve lançar RuntimeException quando ocorrer erro na criação do token")
    void shouldThrowRuntimeExceptionWhenTokenCreationFails() {
        try (MockedStatic<JWT> jwtMockedStatic = mockStatic(JWT.class)) {
            jwtMockedStatic.when(JWT::create).thenThrow(new JWTCreationException("Mocked error", new RuntimeException()));

            RuntimeException exception = assertThrows(RuntimeException.class, () ->
                    tokenService.generateToken("user@test.com")
            );

            assertEquals("token.generation.error", exception.getMessage());
        }
    }

    @Test
    @DisplayName("Deve validar um token gerado corretamente")
    void shouldValidateCorrectToken() {
        String email = "user@test.com";
        String token = tokenService.generateToken(email);

        String subject = tokenService.validateToken(token);

        assertEquals(email, subject);
    }

    @Test
    @DisplayName("Deve lançar InvalidTokenException para token inválido")
    void shouldThrowExceptionForInvalidToken() {
        assertThrows(InvalidTokenException.class, () ->
            tokenService.validateToken("invalid-token")
        );
    }

    @Test
    @DisplayName("Deve lançar InvalidTokenException para token expirado ou com secret diferente")
    void shouldThrowExceptionForModifiedToken() {
        String token = tokenService.generateToken("user@test.com");

        ReflectionTestUtils.setField(tokenService, "secret", "another-secret");

        assertThrows(InvalidTokenException.class, () ->
            tokenService.validateToken(token)
        );
    }
}