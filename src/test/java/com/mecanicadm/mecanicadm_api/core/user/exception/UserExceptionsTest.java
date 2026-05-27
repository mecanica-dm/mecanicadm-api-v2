package com.mecanicadm.mecanicadm_api.core.user.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class UserExceptionsTest {

    @Test
    @DisplayName("Deve cobrir o construtor privado de UserExceptions")
    void shouldCoverPrivateConstructor() throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Constructor<UserExceptions> constructor = UserExceptions.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        UserExceptions instance = constructor.newInstance();
        assertNotNull(instance);
    }

    @Test
    @DisplayName("Deve instanciar todas as exceções de User para garantir cobertura")
    void shouldInstantiateAllExceptions() {
        var notFound = new UserExceptions.NotFound();
        assertEquals(HttpStatus.NOT_FOUND, notFound.getStatus());
        assertEquals("user.not.found", notFound.getMessageKey());

        var emailExists = new UserExceptions.EmailExists();
        assertEquals(HttpStatus.CONFLICT, emailExists.getStatus());
        assertEquals("user.email.exists", emailExists.getMessageKey());

        var emailNotEmpty = new UserExceptions.EmailNotEmpty();
        assertEquals(HttpStatus.BAD_REQUEST, emailNotEmpty.getStatus());
        assertEquals("user.email.not.empty", emailNotEmpty.getMessageKey());

        var passwordMinLength = new UserExceptions.PasswordMinLength();
        assertEquals(HttpStatus.BAD_REQUEST, passwordMinLength.getStatus());
        assertEquals("user.password.min.length", passwordMinLength.getMessageKey());

        var currentPasswordRequired = new UserExceptions.CurrentPasswordRequired();
        assertEquals(HttpStatus.BAD_REQUEST, currentPasswordRequired.getStatus());
        assertEquals("user.password.current.required", currentPasswordRequired.getMessageKey());

        var badCredentials = new UserExceptions.BadCredentials();
        assertEquals(HttpStatus.UNAUTHORIZED, badCredentials.getStatus());
        assertEquals("error.bad.credentials", badCredentials.getMessageKey());

        var tokenInvalid = new UserExceptions.TokenInvalid();
        assertEquals(HttpStatus.BAD_REQUEST, tokenInvalid.getStatus());
        assertEquals("token.invalid", tokenInvalid.getMessageKey());

        var tokenExpired = new UserExceptions.TokenExpired();
        assertEquals(HttpStatus.BAD_REQUEST, tokenExpired.getStatus());
        assertEquals("token.expired", tokenExpired.getMessageKey());
    }
}