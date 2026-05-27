package com.mecanicadm.mecanicadm_api.core.client.service;

import com.mecanicadm.mecanicadm_api.core.client.adapter.repository.ClientRepository;
import com.mecanicadm.mecanicadm_api.core.client.domain.Client;
import com.mecanicadm.mecanicadm_api.core.client.exception.ClientExceptions;
import com.mecanicadm.mecanicadm_api.core.client.usecase.CreateClientUseCase;
import com.mecanicadm.mecanicadm_api.core.client.usecase.command.CreateClientCommand;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.UUID;

@Service
public class CreateClientService implements CreateClientUseCase {

    private final ClientRepository clientRepository;

    public CreateClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Override
    @Transactional
    public UUID handle(CreateClientCommand cmd) {
        validate(cmd);

        clientRepository.findClientByEmail(cmd.email()).ifPresent(c -> {
            throw new ClientExceptions.EmailExists();
        });

        clientRepository.findClientByDocument(cmd.document()).ifPresent(c -> {
            throw new ClientExceptions.DocumentExists();
        });

        Client client = Client.create(
                cmd.name(),
                cmd.email(),
                cmd.document(),
                cmd.phone()
        );

        client = clientRepository.save(client);

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
