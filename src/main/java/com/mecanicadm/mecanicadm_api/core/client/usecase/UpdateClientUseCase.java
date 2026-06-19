package com.mecanicadm.mecanicadm_api.core.client.usecase;

import com.mecanicadm.mecanicadm_api.core.client.domain.Client;
import com.mecanicadm.mecanicadm_api.core.client.domain.port.ClientGateway;
import com.mecanicadm.mecanicadm_api.core.client.exception.ClientExceptions;
import com.mecanicadm.mecanicadm_api.core.client.usecase.command.UpdateClientCommand;

import static java.util.Objects.requireNonNullElse;

public class UpdateClientUseCase {

    private final ClientGateway gateway;

    public UpdateClientUseCase(ClientGateway gateway) {
        this.gateway = gateway;
    }

    public void execute(UpdateClientCommand cmd) {
        Client client = gateway.findById(cmd.id())
                .orElseThrow(ClientExceptions.NotFound::new);

        if (cmd.document() != null && !cmd.document().equals(client.getDocument())) {
            gateway.findClientByDocument(cmd.document()).ifPresent(existingClient -> {
                if (!existingClient.getId().equals(cmd.id())) {
                    throw new ClientExceptions.DocumentExists();
                }
            });
        }

        if (cmd.email() != null && !cmd.email().equals(client.getEmail())) {
            gateway.findClientByEmail(cmd.email()).ifPresent(existingClient -> {
                if (!existingClient.getId().equals(cmd.id())) {
                    throw new ClientExceptions.EmailExists();
                }
            });
        }

        client.update(
                requireNonNullElse(cmd.name(), client.getName()),
                requireNonNullElse(cmd.email(), client.getEmail()),
                requireNonNullElse(cmd.document(), client.getDocument()),
                requireNonNullElse(cmd.phone(), client.getPhone())
        );

        gateway.update(client);
    }
}
