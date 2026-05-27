package com.mecanicadm.mecanicadm_api.core.client.service;

import com.mecanicadm.mecanicadm_api.core.client.adapter.repository.ClientRepository;
import com.mecanicadm.mecanicadm_api.core.client.domain.Client;
import com.mecanicadm.mecanicadm_api.core.client.exception.ClientExceptions;
import com.mecanicadm.mecanicadm_api.core.client.usecase.command.CreateClientCommand;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateClientServiceTest {

    @Mock
    private ClientRepository repository;

    @InjectMocks
    private CreateClientService createClientService;

    private CreateClientCommand command;

    @BeforeEach
    void setUp() {
        command = new CreateClientCommand(
                "Test Client",
                "test@example.com",
                "17871234053",
                "48999999999"
        );
    }

    @Test
    @DisplayName("Deve criar um cliente com sucesso quando e-mail e Documento não existem")
    void shouldCreateClientSuccessfully() {
        when(repository.findClientByEmail(command.email())).thenReturn(Optional.empty());
        when(repository.findClientByDocument(command.document())).thenReturn(Optional.empty());

        Client savedClient = mock(Client.class);
        UUID expectedId = UUID.randomUUID();
        when(savedClient.getId()).thenReturn(expectedId);
        when(repository.save(any(Client.class))).thenReturn(savedClient);

        UUID resultId = createClientService.handle(command);

        assertNotNull(resultId);
        assertEquals(expectedId, resultId);
        verify(repository).findClientByEmail(command.email());
        verify(repository).findClientByDocument(command.document());
        verify(repository).save(any(Client.class));
    }

    @Test
    @DisplayName("Deve lançar exceção quando o e-mail já está em uso")
    void shouldThrowExceptionWhenEmailExists() {
        when(repository.findClientByEmail(command.email())).thenReturn(Optional.of(mock(Client.class)));

        assertThrows(ClientExceptions.EmailExists.class, () -> createClientService.handle(command));

        verify(repository).findClientByEmail(command.email());
        verify(repository, never()).save(any(Client.class));
    }

    @Test
    @DisplayName("Deve lançar exceção quando o Documento já está em uso")
    void shouldThrowExceptionWhenDocumentExists() {
        when(repository.findClientByEmail(command.email())).thenReturn(Optional.empty());
        when(repository.findClientByDocument(command.document())).thenReturn(Optional.of(mock(Client.class)));

        assertThrows(ClientExceptions.DocumentExists.class, () -> createClientService.handle(command));

        verify(repository).findClientByDocument(command.document());
        verify(repository, never()).save(any(Client.class));
    }

    @Test
    @DisplayName("Deve lançar exceção quando o nome do cliente é vazio")
    void shouldThrowExceptionWhenNameIsEmpty() {
        CreateClientCommand invalidCommand = new CreateClientCommand(
                "",
                "test@example.com",
                "17871234053",
                "48999999999"
        );
        assertThrows(ClientExceptions.NameNotEmpty.class, () -> createClientService.handle(invalidCommand));
        verify(repository, never()).findClientByEmail(anyString());
        verify(repository, never()).findClientByDocument(anyString());
        verify(repository, never()).save(any(Client.class));
    }

    @Test
    @DisplayName("Deve lançar exceção quando o e-mail do cliente é vazio")
    void shouldThrowExceptionWhenEmailIsEmpty() {
        CreateClientCommand invalidCommand = new CreateClientCommand(
                "Test Client",
                "",
                "17871234053",
                "48999999999"
        );
        assertThrows(ClientExceptions.EmailNotEmpty.class, () -> createClientService.handle(invalidCommand));
        verify(repository, never()).findClientByEmail(anyString());
        verify(repository, never()).findClientByDocument(anyString());
        verify(repository, never()).save(any(Client.class));
    }

    @Test
    @DisplayName("Deve lançar exceção quando o Documento do cliente é vazio")
    void shouldThrowExceptionWhenDocumentIsEmpty() {
        CreateClientCommand invalidCommand = new CreateClientCommand(
                "Test Client",
                "test@example.com",
                "",
                "48999999999"
        );
        assertThrows(ClientExceptions.DocumentNotEmpty.class, () -> createClientService.handle(invalidCommand));
        verify(repository, never()).findClientByEmail(anyString());
        verify(repository, never()).findClientByDocument(anyString());
        verify(repository, never()).save(any(Client.class));
    }
}
