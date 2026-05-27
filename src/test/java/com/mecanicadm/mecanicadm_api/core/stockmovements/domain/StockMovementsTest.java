package com.mecanicadm.mecanicadm_api.core.stockmovements.domain;

import com.mecanicadm.mecanicadm_api.core.stockmovements.domain.enums.MovementType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class StockMovementsTest {

    @Test
    @DisplayName("Deve criar uma movimentação de adição com sucesso")
    void shouldCreateAdditionMovementSuccessfully() {
        UUID materialId = UUID.randomUUID();
        Integer quantity = 10;

        StockMovements movement = StockMovements.recordAddition(materialId, quantity);

        assertNotNull(movement);
        assertEquals(materialId, movement.getMaterialId());
        assertEquals(quantity, movement.getQuantity());
        assertEquals(MovementType.ADDITION, movement.getType());
    }

    @Test
    @DisplayName("Deve criar uma movimentação de redução com sucesso")
    void shouldCreateReductionMovementSuccessfully() {
        UUID materialId = UUID.randomUUID();
        Integer quantity = 5;

        StockMovements movement = StockMovements.recordReduction(materialId, null, quantity);

        assertNotNull(movement);
        assertEquals(materialId, movement.getMaterialId());
        assertEquals(quantity, movement.getQuantity());
        assertEquals(MovementType.REDUCTION, movement.getType());
    }

    @Test
    @DisplayName("Deve retornar true para deletedAt quando presente")
    void shouldReturnTrueWhenDeletedAtIsPresent() {
        StockMovements movement = StockMovements.recordAddition(UUID.randomUUID(), 10);

        assertFalse(movement.getDeletedAt().isPresent());

        ReflectionTestUtils.setField(movement, "deletedAt", LocalDateTime.now());

        assertTrue(movement.getDeletedAt().isPresent());
    }
}
