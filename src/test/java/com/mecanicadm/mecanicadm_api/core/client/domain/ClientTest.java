package com.mecanicadm.mecanicadm_api.core.client.domain;

import com.mecanicadm.mecanicadm_api.core.client.exception.ClientExceptions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ClientTest {

    @Test
    @DisplayName("Deve criar um cliente com sucesso")
    void shouldCreateClient() {
        Client client = Client.create("Test Client", "test@example.com", "17871234053", "48999999999");

        assertEquals("Test Client", client.getName());
        assertEquals("test@example.com", client.getEmail());
        assertEquals("17871234053", client.getDocument());
        assertEquals("48999999999", client.getPhone());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"  ", "\t", "\n"})
    @DisplayName("Deve lançar exceção ao criar cliente com nome nulo ou vazio")
    void shouldThrowExceptionForInvalidName(String invalidName) {
        assertThrows(ClientExceptions.NameNotEmpty.class, () ->
                Client.create(invalidName, "test@example.com", "17871234053", "48999999999"));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"  ", "\t", "\n"})
    @DisplayName("Deve lançar exceção ao criar cliente com e-mail nulo ou vazio")
    void shouldThrowExceptionForInvalidEmail(String invalidEmail) {
        assertThrows(ClientExceptions.EmailNotEmpty.class, () ->
                Client.create("Test Client", invalidEmail, "17871234053", "48999999999"));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"  ", "\t", "\n"})
    @DisplayName("Deve lançar exceção ao criar cliente com documento nulo ou vazio")
    void shouldThrowExceptionForInvalidDocument(String invalidDocument) {
        assertThrows(ClientExceptions.DocumentNotEmpty.class, () ->
                Client.create("Test Client", "test@example.com", invalidDocument, "48999999999"));
    }

    @Test
    @DisplayName("Deve atualizar informações do cliente")
    void shouldUpdateClientInfo() {
        Client client = Client.create("Old Name", "old@example.com", "17871234053", "48999999999");

        client.update("New Name", "new@example.com", "43615632009", "48888888888");

        assertEquals("New Name", client.getName());
        assertEquals("new@example.com", client.getEmail());
        assertEquals("43615632009", client.getDocument());
        assertEquals("48888888888", client.getPhone());
    }
}
