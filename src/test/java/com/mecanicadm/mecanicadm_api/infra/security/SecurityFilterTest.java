package com.mecanicadm.mecanicadm_api.infra.security;

import com.mecanicadm.mecanicadm_api.core.user.domain.User;
import com.mecanicadm.mecanicadm_api.core.user.domain.port.UserGateway;
import com.mecanicadm.mecanicadm_api.infra.services.TokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SecurityFilterTest {

    private SecurityFilter securityFilter;
    private TokenService tokenService;
    private UserGateway userGateway;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private FilterChain filterChain;

    @BeforeEach
    void setUp() {
        tokenService = mock(TokenService.class);
        userGateway = mock(UserGateway.class);
        securityFilter = new SecurityFilter(tokenService, userGateway);
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        filterChain = mock(FilterChain.class);
        SecurityContextHolder.clearContext();
    }

    @Test
    @DisplayName("Deve autenticar o usuário quando um token válido é fornecido")
    void shouldAuthenticateUserWhenValidTokenProvided() throws Exception {
        String token = "valid-token";
        String email = "user@test.com";
        User user = User.create(email, "password", "Test User", mock(PasswordEncoder.class));
        UserAdapter userAdapter = new UserAdapter(user);

        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(tokenService.validateToken(token)).thenReturn(email);
        when(userGateway.findByEmail(email)).thenReturn(Optional.of(user));

        securityFilter.doFilterInternal(request, response, filterChain);

        assertNotNull(SecurityContextHolder.getContext().getAuthentication());
        assertEquals(userAdapter, SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        verify(filterChain).doFilter(request, response);
    }

    @Test
    @DisplayName("Deve retornar 401 quando o token é inválido")
    void shouldReturn401WhenTokenIsInvalid() throws Exception {
        String token = "invalid-token";
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);

        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(tokenService.validateToken(token)).thenThrow(new RuntimeException("Token inválido"));
        when(response.getWriter()).thenReturn(writer);

        securityFilter.doFilterInternal(request, response, filterChain);

        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(filterChain, never()).doFilter(request, response);
    }

    @Test
    @DisplayName("Deve continuar a cadeia de filtros sem autenticar quando não há token")
    void shouldContinueFilterChainWhenNoTokenProvided() throws Exception {
        when(request.getHeader("Authorization")).thenReturn(null);

        securityFilter.doFilterInternal(request, response, filterChain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(filterChain).doFilter(request, response);
    }

    @Test
    @DisplayName("Deve continuar a cadeia de filtros se o usuário do token não for encontrado")
    void shouldContinueFilterChainWhenUserNotFound() throws Exception {
        String token = "valid-token";
        String email = "notfound@test.com";

        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(tokenService.validateToken(token)).thenReturn(email);
        when(userGateway.findByEmail(email)).thenReturn(Optional.empty());

        securityFilter.doFilterInternal(request, response, filterChain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(filterChain).doFilter(request, response);
    }
}
