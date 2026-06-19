package com.mecanicadm.mecanicadm_api.core.user.domain;

import com.mecanicadm.mecanicadm_api.core.user.domain.enums.UserRole;
import com.mecanicadm.mecanicadm_api.core.user.exception.UserExceptions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserTest {

    @Test
    @DisplayName("Deve criar um usuário com sucesso")
    void shouldCreateUser() {
        PasswordEncoder encoder = mock(PasswordEncoder.class);
        when(encoder.encode("password123")).thenReturn("encodedPassword");

        User user = User.create("test@example.com", "password123", "Test Name", encoder);

        assertEquals("test@example.com", user.getEmail());
        assertEquals("encodedPassword", user.getPassword());
        assertEquals("Test Name", user.getName());
        assertTrue(user.getRoles().contains(UserRole.USER));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"  ", "\t", "\n"})
    @DisplayName("Deve lançar exceção ao criar usuário com senha nula, vazia ou apenas espaços")
    void shouldThrowExceptionForInvalidPasswordText(String invalidPassword) {
        PasswordEncoder encoder = mock(PasswordEncoder.class);
        assertThrows(UserExceptions.PasswordMinLength.class, () -> 
            User.create("test@example.com", invalidPassword, "Test Name", encoder));
    }

    @Test
    @DisplayName("Deve lançar exceção ao criar usuário com senha curta")
    void shouldThrowExceptionForShortPassword() {
        PasswordEncoder encoder = mock(PasswordEncoder.class);
        assertThrows(UserExceptions.PasswordMinLength.class, () -> 
            User.create("test@example.com", "12345", "Test Name", encoder));
    }

    @Test
    @DisplayName("Deve lançar exceção ao criar usuário com e-mail vazio")
    void shouldThrowExceptionForEmptyEmail() {
        PasswordEncoder encoder = mock(PasswordEncoder.class);
        assertThrows(UserExceptions.EmailNotEmpty.class, () -> 
            User.create("", "password123", "Test Name", encoder));
    }

    @Test
    @DisplayName("Deve atualizar informações do usuário")
    void shouldUpdateUserInfo() {
        PasswordEncoder encoder = mock(PasswordEncoder.class);
        when(encoder.encode(anyString())).thenReturn("encoded");
        User user = User.create("old@email.com", "password123", "Old Name", encoder);

        user.updateInfo("New Name", "new@email.com");

        assertEquals("New Name", user.getName());
        assertEquals("new@email.com", user.getEmail());
    }

    @Test
    @DisplayName("Deve gerenciar roles do usuário")
    void shouldManageRoles() {
        PasswordEncoder encoder = mock(PasswordEncoder.class);
        when(encoder.encode(anyString())).thenReturn("encoded");
        User user = User.create("test@email.com", "password123", "Name", encoder);

        user.addRole(UserRole.ATTENDANT);
        assertTrue(user.getRoles().contains(UserRole.ATTENDANT));

        user.removeRole(UserRole.ATTENDANT);
        assertFalse(user.getRoles().contains(UserRole.ATTENDANT));
    }

    @Test
    @DisplayName("Deve retornar true para isDeleted quando deletedAt estiver preenchido")
    void shouldReturnTrueWhenDeletedAtIsPresent() {
        PasswordEncoder encoder = mock(PasswordEncoder.class);
        when(encoder.encode(anyString())).thenReturn("encoded");
        User user = User.create("test@email.com", "password123", "Name", encoder);

        assertFalse(user.isDeleted());

        ReflectionTestUtils.setField(user, "deletedAt", LocalDateTime.now());

        assertTrue(user.isDeleted());
    }
}
