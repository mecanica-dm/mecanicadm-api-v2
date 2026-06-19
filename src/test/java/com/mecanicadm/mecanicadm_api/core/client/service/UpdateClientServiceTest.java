package com.mecanicadm.mecanicadm_api.core.client.service;

import com.mecanicadm.mecanicadm_api.core.client.domain.Client;
import com.mecanicadm.mecanicadm_api.core.client.domain.port.ClientGateway;
import com.mecanicadm.mecanicadm_api.core.client.exception.ClientExceptions;
import com.mecanicadm.mecanicadm_api.core.client.usecase.UpdateClientUseCase;
import com.mecanicadm.mecanicadm_api.core.client.usecase.command.UpdateClientCommand;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UpdateClientServiceTest {

    @Mock
    private ClientGateway repository;

    private UpdateClientUseCase updateClientUseCase;

    private UpdateClientCommand command;
    private UUID clientId;
    private Client existingClient;

    @BeforeEach
    void setUp() {
        clientId = UUID.randomUUID();
        updateClientUseCase = new UpdateClientUseCase(repository);
        command = new UpdateClientCommand(
                clientId,
                "Updated Client",
                "updated@example.com",
                "43615632009",
                "48988888888"
        );
        existingClient = mock(Client.class);
    }

    @Test
    @DisplayName("Deve atualizar o cliente com sucesso")
    void shouldUpdateClientSuccessfully() {
        when(repository.findById(clientId)).thenReturn(Optional.of(existingClient));
        when(existingClient.getName()).thenReturn("Old Name");
        when(existingClient.getDocument()).thenReturn("17871234053");
        when(existingClient.getEmail()).thenReturn("old@example.com");
        when(existingClient.getPhone()).thenReturn("48999999999");

        when(repository.findClientByDocument(command.document())).thenReturn(Optional.empty());
        when(repository.findClientByEmail(command.email())).thenReturn(Optional.empty());

        updateClientUseCase.execute(command);

        verify(repository).findById(clientId);
        verify(existingClient).update(command.name(), command.email(), command.document(), command.phone());
        verify(repository).update(existingClient);
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar atualizar um cliente inexistente")
    void shouldThrowExceptionWhenClientNotFound() {
        when(repository.findById(clientId)).thenReturn(Optional.empty());

        assertThrows(ClientExceptions.NotFound.class, () -> updateClientUseCase.execute(command));

        verify(repository).findById(clientId);
        verify(repository, never()).update(any(Client.class));
    }

    @Test
    @DisplayName("Deve permitir atualização quando Documento e E-mail forem nulos no comando")
    void shouldUpdateSuccessfullyWhenDocumentAndEmailAreNull() {
        UpdateClientCommand nullCmd = new UpdateClientCommand(clientId, "New Name", null, null, null);
        when(repository.findById(clientId)).thenReturn(Optional.of(existingClient));

        when(existingClient.getName()).thenReturn("Old Name");
        when(existingClient.getEmail()).thenReturn("old@example.com");
        when(existingClient.getDocument()).thenReturn("17871234053");
        when(existingClient.getPhone()).thenReturn("48999999999");

        assertDoesNotThrow(() -> updateClientUseCase.execute(nullCmd));

        verify(repository, never()).findClientByDocument(anyString());
        verify(repository, never()).findClientByEmail(anyString());
    }

    @Test
    @DisplayName("Deve permitir atualização quando Documento e E-mail forem iguais aos atuais")
    void shouldUpdateSuccessfullyWhenDocumentAndEmailAreSame() {
        when(repository.findById(clientId)).thenReturn(Optional.of(existingClient));
        when(existingClient.getDocument()).thenReturn(command.document());
        when(existingClient.getEmail()).thenReturn(command.email());

        when(existingClient.getName()).thenReturn("Old Name");
        when(existingClient.getPhone()).thenReturn("48999999999");

        assertDoesNotThrow(() -> updateClientUseCase.execute(command));

        verify(repository, never()).findClientByDocument(anyString());
        verify(repository, never()).findClientByEmail(anyString());
    }

    @Test
    @DisplayName("Deve permitir atualização quando Documento pertence ao próprio cliente")
    void shouldUpdateSuccessfullyWhenDocumentBelongsToSelf() {
        when(repository.findById(clientId)).thenReturn(Optional.of(existingClient));
        when(existingClient.getDocument()).thenReturn("old-document");
        when(existingClient.getEmail()).thenReturn(command.email());

        when(existingClient.getName()).thenReturn("Old Name");
        when(existingClient.getPhone()).thenReturn("48999999999");

        when(existingClient.getId()).thenReturn(clientId);
        when(repository.findClientByDocument(command.document())).thenReturn(Optional.of(existingClient));

        assertDoesNotThrow(() -> updateClientUseCase.execute(command));
    }

    @Test
    @DisplayName("Deve permitir atualização quando E-mail pertence ao próprio cliente")
    void shouldUpdateSuccessfullyWhenEmailBelongsToSelf() {
        when(repository.findById(clientId)).thenReturn(Optional.of(existingClient));
        when(existingClient.getDocument()).thenReturn(command.document());
        when(existingClient.getEmail()).thenReturn("old@email.com");

        when(existingClient.getName()).thenReturn("Old Name");
        when(existingClient.getPhone()).thenReturn("48999999999");

        when(existingClient.getId()).thenReturn(clientId);
        when(repository.findClientByEmail(command.email())).thenReturn(Optional.of(existingClient));

        assertDoesNotThrow(() -> updateClientUseCase.execute(command));
    }

    @Test
    @DisplayName("Deve lançar exceção quando o novo e-mail já está em uso por outro cliente")
    void shouldThrowExceptionWhenEmailExistsForOtherClient() {
        when(repository.findById(clientId)).thenReturn(Optional.of(existingClient));

        when(existingClient.getDocument()).thenReturn(command.document());
        when(existingClient.getEmail()).thenReturn("old@example.com");

        Client anotherClient = mock(Client.class);
        when(anotherClient.getId()).thenReturn(UUID.randomUUID());
        when(repository.findClientByEmail(command.email())).thenReturn(Optional.of(anotherClient));

        assertThrows(ClientExceptions.EmailExists.class, () -> updateClientUseCase.execute(command));
    }

    @Test
    @DisplayName("Deve lançar exceção quando o novo Documento já está em uso por outro cliente")
    void shouldThrowExceptionWhenDocumentExistsForOtherClient() {
        when(repository.findById(clientId)).thenReturn(Optional.of(existingClient));
        when(existingClient.getDocument()).thenReturn("old-document");

        Client anotherClient = mock(Client.class);
        when(anotherClient.getId()).thenReturn(UUID.randomUUID());
        when(repository.findClientByDocument(command.document())).thenReturn(Optional.of(anotherClient));

        assertThrows(ClientExceptions.DocumentExists.class, () -> updateClientUseCase.execute(command));
    }
}
