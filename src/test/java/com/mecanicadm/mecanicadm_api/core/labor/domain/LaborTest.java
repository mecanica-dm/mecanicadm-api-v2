package com.mecanicadm.mecanicadm_api.core.labor.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class LaborTest {

    @Test
    @DisplayName("Deve criar um serviço com sucesso")
    void shouldCreateLaborSuccessfully() {
        Labor labor = Labor.create(
                "Troca de Óleo",
                new BigDecimal("50.00")
        );

        assertNotNull(labor);
        assertEquals("Troca de Óleo", labor.getName());
        assertEquals(new BigDecimal("50.00"), labor.getPrice());
    }

    @Test
    @DisplayName("Deve atualizar as informações do serviço com sucesso")
    void shouldUpdateLaborSuccessfully() {
        Labor labor = Labor.create(
                "Troca de Óleo",
                new BigDecimal("50.00")
        );

        labor.update(
                "Revisão Geral",
                new BigDecimal("150.00")
        );

        assertEquals("Revisão Geral", labor.getName());
        assertEquals(new BigDecimal("150.00"), labor.getPrice());
    }
}
