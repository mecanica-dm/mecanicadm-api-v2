package com.mecanicadm.mecanicadm_api.core.client.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ClientExceptionsTest {

    @Test
    @DisplayName("Deve cobrir o construtor privado de ClientExceptions")
    void shouldCoverPrivateConstructor() throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Constructor<ClientExceptions> constructor = ClientExceptions.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        ClientExceptions instance = constructor.newInstance();
        assertNotNull(instance);
    }

    @Test
    @DisplayName("Deve instanciar todas as exceções de Client para garantir cobertura")
    void shouldInstantiateAllExceptions() {
        var notFound = new ClientExceptions.NotFound();
        assertEquals(HttpStatus.NOT_FOUND, notFound.getStatus());
        assertEquals("client.not.found", notFound.getMessageKey());

        var documentExists = new ClientExceptions.DocumentExists();
        assertEquals(HttpStatus.CONFLICT, documentExists.getStatus());
        assertEquals("client.document.exists", documentExists.getMessageKey());

        var emailExists = new ClientExceptions.EmailExists();
        assertEquals(HttpStatus.CONFLICT, emailExists.getStatus());
        assertEquals("client.email.exists", emailExists.getMessageKey());

        var nameNotEmpty = new ClientExceptions.NameNotEmpty();
        assertEquals(HttpStatus.BAD_REQUEST, nameNotEmpty.getStatus());
        assertEquals("client.name.not.empty", nameNotEmpty.getMessageKey());

        var emailNotEmpty = new ClientExceptions.EmailNotEmpty();
        assertEquals(HttpStatus.BAD_REQUEST, emailNotEmpty.getStatus());
        assertEquals("client.email.not.empty", emailNotEmpty.getMessageKey());

        var documentNotEmpty = new ClientExceptions.DocumentNotEmpty();
        assertEquals(HttpStatus.BAD_REQUEST, documentNotEmpty.getStatus());
        assertEquals("client.document.not.empty", documentNotEmpty.getMessageKey());
    }
}
