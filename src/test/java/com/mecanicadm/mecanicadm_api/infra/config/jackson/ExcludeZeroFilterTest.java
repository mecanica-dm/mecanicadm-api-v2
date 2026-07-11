package com.mecanicadm.mecanicadm_api.infra.config.jackson;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ExcludeZeroFilterTest {

    private final ExcludeZeroFilter filter = new ExcludeZeroFilter();

    @Test
    @DisplayName("Deve retornar true quando comparado com null")
    void shouldReturnTrueWhenComparedWithNull() {
        assertTrue(filter.equals(null));
    }

    @Test
    @DisplayName("Deve retornar true quando Number for zero")
    void shouldReturnTrueWhenNumberIsZero() {
        assertTrue(filter.equals(0.0));
    }

    @Test
    @DisplayName("Deve retornar true quando Integer zero")
    void shouldReturnTrueWhenIntegerIsZero() {
        assertTrue(filter.equals(Integer.valueOf(0)));
    }

    @Test
    @DisplayName("Deve retornar true quando Long zero")
    void shouldReturnTrueWhenLongIsZero() {
        assertTrue(filter.equals(Long.valueOf(0L)));
    }

    @Test
    @DisplayName("Deve retornar false quando Number nao for zero")
    void shouldReturnFalseWhenNumberIsNotZero() {
        assertFalse(filter.equals(1.0));
    }

    @Test
    @DisplayName("Deve retornar false quando Integer nao for zero")
    void shouldReturnFalseWhenIntegerIsNotZero() {
        assertFalse(filter.equals(Integer.valueOf(5)));
    }

    @Test
    @DisplayName("Deve retornar false quando objeto nao for Number")
    void shouldReturnFalseWhenObjectIsNotNumber() {
        assertFalse(filter.equals("string"));
    }

    @Test
    @DisplayName("Deve retornar false quando objeto for outro tipo")
    void shouldReturnFalseWhenObjectIsDifferentType() {
        assertFalse(filter.equals(new Object()));
    }

    @Test
    @DisplayName("Deve retornar hashCode consistente")
    void shouldReturnConsistentHashCode() {
        int hash1 = filter.hashCode();
        int hash2 = filter.hashCode();
        assertEquals(hash1, hash2);
        assertEquals(ExcludeZeroFilter.class.hashCode(), hash1);
    }

    @Test
    @DisplayName("Deve retornar false quando comparado com Double negativo")
    void shouldReturnFalseWhenComparedWithNegativeDouble() {
        assertFalse(filter.equals(-1.0));
    }

    @Test
    @DisplayName("Deve retornar true quando comparado com Float zero")
    void shouldReturnTrueWhenComparedWithFloatZero() {
        assertTrue(filter.equals(0.0f));
    }
}
