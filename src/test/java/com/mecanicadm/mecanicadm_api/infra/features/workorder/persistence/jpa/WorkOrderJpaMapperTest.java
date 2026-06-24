package com.mecanicadm.mecanicadm_api.infra.features.workorder.persistence.jpa;

import com.mecanicadm.mecanicadm_api.core.workorder.domain.WorkOrder;
import com.mecanicadm.mecanicadm_api.core.workorder.domain.WorkOrderBudget;
import com.mecanicadm.mecanicadm_api.core.workorder.domain.WorkOrderLaborItem;
import com.mecanicadm.mecanicadm_api.core.workorder.domain.WorkOrderMaterialItem;
import com.mecanicadm.mecanicadm_api.core.workorder.domain.enums.LaborExecutionStatus;
import com.mecanicadm.mecanicadm_api.core.workorder.domain.enums.WorkOrderBudgetStatus;
import com.mecanicadm.mecanicadm_api.core.workorder.domain.enums.WorkOrderStatus;
import com.mecanicadm.mecanicadm_api.infra.features.workorder.persistence.entity.WorkOrderBudgetJpaEntity;
import com.mecanicadm.mecanicadm_api.infra.features.workorder.persistence.entity.WorkOrderJpaEntity;
import com.mecanicadm.mecanicadm_api.infra.features.workorder.persistence.entity.WorkOrderLaborItemJpaEntity;
import com.mecanicadm.mecanicadm_api.infra.features.workorder.persistence.entity.WorkOrderMaterialItemJpaEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class WorkOrderJpaMapperTest {

    @Test
    @DisplayName("Deve mapear JpaEntity para domínio com todos os relacionamentos")
    void shouldMapToDomainWithAllRelations() {
        var id = UUID.randomUUID();
        var clientId = UUID.randomUUID();
        var now = LocalDateTime.now();
        var laborItemId = UUID.randomUUID();
        var materialItemId = UUID.randomUUID();

        var laborEntity = new WorkOrderLaborItemJpaEntity(laborItemId, UUID.randomUUID(), now, now.plusHours(1), LaborExecutionStatus.EXECUTION_COMPLETED);
        var materialEntity = new WorkOrderMaterialItemJpaEntity(materialItemId, UUID.randomUUID(), 5);
        var budgetEntity = new WorkOrderBudgetJpaEntity(id, new BigDecimal("500.00"), WorkOrderBudgetStatus.APPROVED, null);

        var entity = new WorkOrderJpaEntity(id, clientId, "ABC-1234", "Troca de óleo", WorkOrderStatus.IN_EXECUTION, now, null, Set.of(laborEntity), Set.of(materialEntity), budgetEntity);
        entity.setDateCreated(now);
        entity.setDateUpdated(now);
        entity.setDeletedAt(null);

        var domain = WorkOrderJpaMapper.toDomain(entity);

        assertEquals(id, domain.getId());
        assertEquals(clientId, domain.getClientId());
        assertEquals("ABC-1234", domain.getVehicleId());
        assertEquals("Troca de óleo", domain.getDescription());
        assertEquals(WorkOrderStatus.IN_EXECUTION, domain.getStatus());
        assertEquals(now, domain.getExecutionStartAt().orElse(null));
        assertTrue(domain.getExecutionEndAt().isEmpty());
        assertEquals(now, domain.getDateCreated());
        assertEquals(now, domain.getDateUpdated());
        assertNull(domain.getDeletedAt());
        assertEquals(1, domain.getLaborItems().size());
        assertEquals(1, domain.getMaterialItems().size());
        assertTrue(domain.getBudget().isPresent());
        assertEquals(new BigDecimal("500.00"), domain.getBudget().get().getTotalPrice());
    }

    @Test
    @DisplayName("Deve mapear domínio para JpaEntity com todos os relacionamentos")
    void shouldMapToEntityWithAllRelations() {
        var id = UUID.randomUUID();
        var clientId = UUID.randomUUID();
        var now = LocalDateTime.now();

        var laborItem = WorkOrderLaborItem.restore(UUID.randomUUID(), UUID.randomUUID(), now, now.plusHours(2), LaborExecutionStatus.EXECUTION_COMPLETED);
        var materialItem = WorkOrderMaterialItem.restore(UUID.randomUUID(), UUID.randomUUID(), 3);
        var budget = WorkOrderBudget.restore(id, new BigDecimal("750.00"), WorkOrderBudgetStatus.PENDING, null);
        var domain = WorkOrder.restore(id, clientId, "XYZ-9876", "Revisão completa", WorkOrderStatus.RECEIVED, null, null, Set.of(laborItem), Set.of(materialItem), budget, now, now, null);

        var entity = WorkOrderJpaMapper.toEntity(domain);

        assertEquals(id, entity.getId());
        assertEquals(clientId, entity.getClientId());
        assertEquals("XYZ-9876", entity.getVehicleId());
        assertEquals("Revisão completa", entity.getDescription());
        assertEquals(WorkOrderStatus.RECEIVED, entity.getStatus());
        assertTrue(entity.getExecutionStartAt().isEmpty());
        assertTrue(entity.getExecutionEndAt().isEmpty());
        assertEquals(1, entity.getLaborItems().size());
        assertEquals(1, entity.getMaterialItems().size());
        assertTrue(entity.getBudget().isPresent());
        assertEquals(new BigDecimal("750.00"), entity.getBudget().get().getTotalPrice());
        assertEquals(now, entity.getDateCreated());
        assertEquals(now, entity.getDateUpdated());
        assertNull(entity.getDeletedAt());
    }

    @Test
    @DisplayName("Deve retornar null quando entidade for nula no toDomain")
    void shouldReturnNullWhenEntityIsNullForToDomain() {
        assertNull(WorkOrderJpaMapper.toDomain(null));
    }

    @Test
    @DisplayName("Deve retornar null quando domínio for nulo no toEntity")
    void shouldReturnNullWhenDomainIsNullForToEntity() {
        assertNull(WorkOrderJpaMapper.toEntity(null));
    }
}
