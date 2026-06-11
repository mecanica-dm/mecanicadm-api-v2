package com.mecanicadm.mecanicadm_api.core.user.usecase;

import com.mecanicadm.mecanicadm_api.infra.features.user.api.dto.UserResponse;
import com.mecanicadm.mecanicadm_api.core.user.domain.port.UserGateway;
import com.mecanicadm.mecanicadm_api.core.user.exception.UserExceptions;
import com.mecanicadm.mecanicadm_api.core.user.usecase.query.FindUserByIdQuery;

public class GetUserByIdUseCase {

    private final UserGateway userGateway;

    public GetUserByIdUseCase(UserGateway userGateway) {
        this.userGateway = userGateway;
    }

    public UserResponse execute(FindUserByIdQuery query) {
        return userGateway.findById(query.id())
                .map(UserResponse::fromEntity)
                .orElseThrow(UserExceptions.NotFound::new);
    }
}
