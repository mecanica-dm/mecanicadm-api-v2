package com.mecanicadm.mecanicadm_api.core.client.domain.port;

import com.mecanicadm.mecanicadm_api.core.client.domain.Client;

import java.util.Optional;
import java.util.UUID;

public interface ClientGateway {
    Client create(Client client);

    Client update(Client client);

    void delete(Client client);

    Optional<Client> findById(UUID id);

    Optional<Client> findClientByDocument(String document);

    Optional<Client> findClientByEmail(String email);

    ClientPageResult findAll(ClientPageQuery pageQuery);
}
