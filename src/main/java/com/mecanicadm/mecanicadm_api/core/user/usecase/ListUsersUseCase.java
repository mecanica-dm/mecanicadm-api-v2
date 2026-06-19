package com.mecanicadm.mecanicadm_api.core.user.usecase;

import com.mecanicadm.mecanicadm_api.core.user.domain.port.UserFilter;
import com.mecanicadm.mecanicadm_api.core.user.domain.port.UserGateway;
import com.mecanicadm.mecanicadm_api.core.user.domain.port.UserPageQuery;
import com.mecanicadm.mecanicadm_api.core.user.domain.port.UserPageResult;
import com.mecanicadm.mecanicadm_api.core.user.usecase.command.ListUsersCommand;

public class ListUsersUseCase {

    private final UserGateway gateway;

    public ListUsersUseCase(UserGateway gateway) {
        this.gateway = gateway;
    }

    public UserPageResult execute(ListUsersCommand command) {
        var filter = new UserFilter(command.name(), command.email());
        var query = new UserPageQuery(filter, command.page(), command.size(), command.sortBy(), command.direction());
        return gateway.findAll(query);
    }
}
