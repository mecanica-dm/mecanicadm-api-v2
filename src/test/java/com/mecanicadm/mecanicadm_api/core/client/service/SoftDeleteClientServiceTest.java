package com.mecanicadm.mecanicadm_api.core.client.service;

import com.mecanicadm.mecanicadm_api.core.client.adapter.repository.ClientRepository;
import com.mecanicadm.mecanicadm_api.core.client.domain.Client;
import com.mecanicadm.mecanicadm_api.core.client.exception.ClientExceptions;
import com.mecanicadm.mecanicadm_api.core.client.usecase.command.SoftDeleteClientCommand;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SoftDeleteClientServiceTest {

    @Mock
    private ClientRepository repository;

    @InjectMocks
    private SoftDeleteClientService softDeleteClientService;

    private UUID clientId;
    private SoftDeleteClientCommand command;

    @BeforeEach
    void setUp() {
        clientId = UUID.randomUUID();
        command = new SoftDeleteClientCommand(clientId);
    }

    @Test
    @DisplayName("Deve realizar a exclusão lógica do cliente com sucesso")
    void shouldSoftDeleteClientSuccessfully() {
        Client client = mock(Client.class);
        when(repository.findById(clientId)).thenReturn(Optional.of(client));

        softDeleteClientService.handle(command);

        verify(repository).findById(clientId);
        verify(repository).delete(client);
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar excluir um cliente inexistente")
    void shouldThrowExceptionWhenClientNotFound() {
        when(repository.findById(clientId)).thenReturn(Optional.empty());

        assertThrows(ClientExceptions.NotFound.class, () -> softDeleteClientService.handle(command));

        verify(repository).findById(clientId);
        verify(repository, never()).delete(any(Client.class));
    }
}
