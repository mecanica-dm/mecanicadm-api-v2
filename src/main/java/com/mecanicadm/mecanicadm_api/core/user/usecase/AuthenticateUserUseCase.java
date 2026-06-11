package com.mecanicadm.mecanicadm_api.core.user.usecase;

import com.mecanicadm.mecanicadm_api.infra.features.user.api.dto.AuthenticationResponse;
import com.mecanicadm.mecanicadm_api.core.user.domain.User;
import com.mecanicadm.mecanicadm_api.core.user.domain.port.UserGateway;
import com.mecanicadm.mecanicadm_api.core.user.exception.UserExceptions;
import com.mecanicadm.mecanicadm_api.core.user.usecase.query.AuthenticateUserQuery;
import com.mecanicadm.mecanicadm_api.infra.services.TokenService;
import org.springframework.security.crypto.password.PasswordEncoder;

public class AuthenticateUserUseCase {

    private final UserGateway userGateway;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    public AuthenticateUserUseCase(UserGateway userGateway, PasswordEncoder passwordEncoder, TokenService tokenService) {
        this.userGateway = userGateway;
        this.passwordEncoder = passwordEncoder;
        this.tokenService = tokenService;
    }

    public AuthenticationResponse execute(AuthenticateUserQuery query) {
        User user = userGateway.findByEmail(query.email())
                .orElseThrow(UserExceptions.BadCredentials::new);

        user.verifyPassword(query.password(), passwordEncoder);

        String token = tokenService.generateToken(user.getEmail());

        return new AuthenticationResponse(
                token,
                user.getId().toString(),
                user.getName(),
                user.getEmail()
        );
    }
}
