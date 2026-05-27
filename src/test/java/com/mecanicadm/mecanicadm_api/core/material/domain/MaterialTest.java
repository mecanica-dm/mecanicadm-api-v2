package com.mecanicadm.mecanicadm_api.core.material.domain;

import com.mecanicadm.mecanicadm_api.core.material.domain.enums.MaterialType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class MaterialTest {

    @Test
    @DisplayName("Deve criar um material com sucesso")
    void shouldCreateMaterialSuccessfully() {
        Material material = Material.create(
                "Óleo de Motor",
                "Castrol",
                "Óleo sintético 5W-30",
                new BigDecimal("80.00"),
                MaterialType.CONSUMABLE
        );

        assertNotNull(material);
        assertEquals("Óleo de Motor", material.getName());
        assertEquals("Castrol", material.getBrand());
        assertEquals("Óleo sintético 5W-30", material.getDescription());
        assertEquals(new BigDecimal("80.00"), material.getPrice());
        assertEquals(MaterialType.CONSUMABLE, material.getType());
    }

    @Test
    @DisplayName("Deve atualizar as informações do material com sucesso")
    void shouldUpdateMaterialSuccessfully() {
        Material material = Material.create(
                "Pneu Velho",
                "Marca Velha",
                "Descrição Velha",
                new BigDecimal("100.00"),
                MaterialType.PART
        );

        material.update(
                "Pneu Novo",
                "Marca Nova",
                "Descrição Nova",
                new BigDecimal("200.00"),
                MaterialType.CONSUMABLE
        );

        assertEquals("Pneu Novo", material.getName());
        assertEquals("Marca Nova", material.getBrand());
        assertEquals("Descrição Nova", material.getDescription());
        assertEquals(new BigDecimal("200.00"), material.getPrice());
        assertEquals(MaterialType.CONSUMABLE, material.getType());
    }

    @Test
    @DisplayName("Deve retornar true para isDeleted quando deletedAt estiver preenchido")
    void shouldReturnTrueWhenDeletedAtIsPresent() {
        Material material = Material.create(
                "Material Teste",
                "Marca Teste",
                "Descrição Teste",
                new BigDecimal("10.00"),
                MaterialType.PART
        );

        assertFalse(material.getDeletedAt().isPresent());

        ReflectionTestUtils.setField(material, "deletedAt", LocalDateTime.now());

        assertTrue(material.getDeletedAt().isPresent());
    }
}
