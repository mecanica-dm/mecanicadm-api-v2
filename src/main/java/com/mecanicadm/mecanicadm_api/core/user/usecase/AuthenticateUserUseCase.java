package com.mecanicadm.mecanicadm_api.core.user.usecase;

import com.mecanicadm.mecanicadm_api.core.user.adapter.api.dto.AuthenticationResponse;
import com.mecanicadm.mecanicadm_api.core.user.usecase.query.AuthenticateUserQuery;

public interface AuthenticateUserUseCase {
    AuthenticationResponse handle(AuthenticateUserQuery query);
}
