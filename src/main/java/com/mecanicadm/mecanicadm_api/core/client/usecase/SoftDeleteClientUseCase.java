package com.mecanicadm.mecanicadm_api.core.client.usecase;

import com.mecanicadm.mecanicadm_api.core.client.domain.Client;
import com.mecanicadm.mecanicadm_api.core.client.domain.port.ClientGateway;
import com.mecanicadm.mecanicadm_api.core.client.exception.ClientExceptions;
import com.mecanicadm.mecanicadm_api.core.client.usecase.command.SoftDeleteClientCommand;

public class SoftDeleteClientUseCase {

    private final ClientGateway gateway;

    public SoftDeleteClientUseCase(ClientGateway gateway) {
        this.gateway = gateway;
    }

    public void execute(SoftDeleteClientCommand cmd) {
        Client client = gateway.findById(cmd.id())
                .orElseThrow(ClientExceptions.NotFound::new);

        gateway.delete(client);
    }
}
