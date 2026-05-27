package com.mecanicadm.mecanicadm_api.core.client.service;

import com.mecanicadm.mecanicadm_api.core.client.adapter.repository.ClientRepository;
import com.mecanicadm.mecanicadm_api.core.client.domain.Client;
import com.mecanicadm.mecanicadm_api.core.client.exception.ClientExceptions;
import com.mecanicadm.mecanicadm_api.core.client.usecase.UpdateClientUseCase;
import com.mecanicadm.mecanicadm_api.core.client.usecase.command.UpdateClientCommand;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static java.util.Objects.requireNonNullElse;

@Service
public class UpdateClientService implements UpdateClientUseCase {

    private final ClientRepository clientRepository;

    public UpdateClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Override
    @Transactional
    public void handle(UpdateClientCommand cmd) {
        Client client = clientRepository.findById(cmd.id())
                .orElseThrow(ClientExceptions.NotFound::new);

        if (cmd.document() != null && !cmd.document().equals(client.getDocument())) {
            clientRepository.findClientByDocument(cmd.document()).ifPresent(existingClient -> {
                if (!existingClient.getId().equals(cmd.id())) {
                    throw new ClientExceptions.DocumentExists();
                }
            });
        }

        if (cmd.email() != null && !cmd.email().equals(client.getEmail())) {
            clientRepository.findClientByEmail(cmd.email()).ifPresent(existingClient -> {
                if (!existingClient.getId().equals(cmd.id())) {
                    throw new ClientExceptions.EmailExists();
                }
            });
        }

        client.updateInfo(
                requireNonNullElse(cmd.name(), client.getName()),
                requireNonNullElse(cmd.email(), client.getEmail()),
                requireNonNullElse(cmd.document(), client.getDocument()),
                requireNonNullElse(cmd.phone(), client.getPhone())
        );

        clientRepository.save(client);
    }
}
