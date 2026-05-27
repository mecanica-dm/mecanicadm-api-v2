package com.mecanicadm.mecanicadm_api.core.user.service;

import com.mecanicadm.mecanicadm_api.core.user.adapter.api.dto.AuthenticationResponse;
import com.mecanicadm.mecanicadm_api.core.user.adapter.repository.UserRepository;
import com.mecanicadm.mecanicadm_api.core.user.domain.User;
import com.mecanicadm.mecanicadm_api.core.user.exception.UserExceptions;
import com.mecanicadm.mecanicadm_api.core.user.usecase.AuthenticateUserUseCase;
import com.mecanicadm.mecanicadm_api.core.user.usecase.query.AuthenticateUserQuery;
import com.mecanicadm.mecanicadm_api.infra.services.TokenService;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticateUserService implements AuthenticateUserUseCase {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    public AuthenticateUserService(UserRepository userRepository, PasswordEncoder passwordEncoder, TokenService tokenService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenService = tokenService;
    }

    @Override
    @Transactional
    public AuthenticationResponse handle(AuthenticateUserQuery query) {
        User user = userRepository.findUserByEmail(query.email())
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
