package com.mecanicadm.mecanicadm_api.core.client.service;

import com.mecanicadm.mecanicadm_api.core.client.adapter.repository.ClientRepository;
import com.mecanicadm.mecanicadm_api.core.client.domain.Client;
import com.mecanicadm.mecanicadm_api.core.client.exception.ClientExceptions;
import com.mecanicadm.mecanicadm_api.core.client.usecase.SoftDeleteClientUseCase;
import com.mecanicadm.mecanicadm_api.core.client.usecase.command.SoftDeleteClientCommand;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SoftDeleteClientService implements SoftDeleteClientUseCase {

    private final ClientRepository clientRepository;

    public SoftDeleteClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Override
    @Transactional
    public void handle(SoftDeleteClientCommand cmd) {
        Client client = clientRepository.findById(cmd.id())
                .orElseThrow(ClientExceptions.NotFound::new);

        clientRepository.delete(client);
    }
}
