package com.mecanicadm.mecanicadm_api.core.user.usecase;

import com.mecanicadm.mecanicadm_api.core.user.domain.User;
import com.mecanicadm.mecanicadm_api.core.user.usecase.query.FindUserByIdQuery;

public interface GetUserByIdUseCase {
    User handle(FindUserByIdQuery query);
}
