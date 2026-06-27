package com.mecanicadm.mecanicadm_api.core.client.usecase;

import com.mecanicadm.mecanicadm_api.core.client.domain.Client;
import com.mecanicadm.mecanicadm_api.core.client.domain.port.ClientGateway;
import com.mecanicadm.mecanicadm_api.core.client.exception.ClientExceptions;
import com.mecanicadm.mecanicadm_api.core.client.usecase.command.CreateClientCommand;
import org.springframework.util.StringUtils;

import java.util.UUID;

public class CreateClientUseCase {

    private final ClientGateway gateway;

    public CreateClientUseCase(ClientGateway gateway) {
        this.gateway = gateway;
    }

    public UUID execute(CreateClientCommand cmd) {
        validate(cmd);

        gateway.findClientByEmail(cmd.email()).ifPresent(c -> {
            throw new ClientExceptions.EmailExists();
        });

        gateway.findClientByDocument(cmd.document()).ifPresent(c -> {
            throw new ClientExceptions.DocumentExists();
        });

        Client client = Client.create(
                cmd.name(),
                cmd.email(),
                cmd.document(),
                cmd.phone()
        );

        client = gateway.create(client);

        return client.getId();
    }

    private void validate(CreateClientCommand cmd) {
        if (!StringUtils.hasText(cmd.name())) {
            throw new ClientExceptions.NameNotEmpty();
        }
        if (!StringUtils.hasText(cmd.email())) {
            throw new ClientExceptions.EmailNotEmpty();
        }
        if (!StringUtils.hasText(cmd.document())) {
            throw new ClientExceptions.DocumentNotEmpty();
        }
    }
}
