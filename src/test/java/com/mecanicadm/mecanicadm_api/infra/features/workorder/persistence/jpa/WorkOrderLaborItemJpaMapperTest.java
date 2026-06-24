package com.mecanicadm.mecanicadm_api.infra.features.workorder.persistence.jpa;

import com.mecanicadm.mecanicadm_api.core.workorder.domain.WorkOrderLaborItem;
import com.mecanicadm.mecanicadm_api.core.workorder.domain.enums.LaborExecutionStatus;
import com.mecanicadm.mecanicadm_api.infra.features.workorder.persistence.entity.WorkOrderLaborItemJpaEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class WorkOrderLaborItemJpaMapperTest {

    @Test
    @DisplayName("Deve mapear JpaEntity para domínio")
    void shouldMapToDomain() {
        var id = UUID.randomUUID();
        var laborId = UUID.randomUUID();
        var now = LocalDateTime.now();
        var entity = new WorkOrderLaborItemJpaEntity(id, laborId, now, now.plusHours(1), LaborExecutionStatus.EXECUTION_COMPLETED);

        var domain = WorkOrderLaborItemJpaMapper.toDomain(entity);

        assertEquals(id, domain.getId());
        assertEquals(laborId, domain.getLaborId());
        assertEquals(now, domain.getExecutionStartAt());
        assertEquals(now.plusHours(1), domain.getExecutionEndAt());
        assertEquals(LaborExecutionStatus.EXECUTION_COMPLETED, domain.getStatus());
    }

    @Test
    @DisplayName("Deve mapear domínio para JpaEntity")
    void shouldMapToEntity() {
        var id = UUID.randomUUID();
        var laborId = UUID.randomUUID();
        var now = LocalDateTime.now();
        var domain = WorkOrderLaborItem.restore(id, laborId, now, now.plusHours(2), LaborExecutionStatus.IN_EXECUTION);

        var entity = WorkOrderLaborItemJpaMapper.toEntity(domain);

        assertEquals(id, entity.getId());
        assertEquals(laborId, entity.getLaborId());
        assertEquals(now, entity.getExecutionStartAt());
        assertEquals(now.plusHours(2), entity.getExecutionEndAt());
        assertEquals(LaborExecutionStatus.IN_EXECUTION, entity.getStatus());
    }

    @Test
    @DisplayName("Deve mapear conjunto de entidades para conjunto de domínios")
    void shouldMapToDomainSet() {
        var id1 = UUID.randomUUID();
        var id2 = UUID.randomUUID();
        var entity1 = new WorkOrderLaborItemJpaEntity(id1, UUID.randomUUID(), null, null, LaborExecutionStatus.AWAITING_EXECUTION);
        var entity2 = new WorkOrderLaborItemJpaEntity(id2, UUID.randomUUID(), null, null, LaborExecutionStatus.AWAITING_EXECUTION);

        var domains = WorkOrderLaborItemJpaMapper.toDomainSet(Set.of(entity1, entity2));

        assertNotNull(domains);
        assertEquals(2, domains.size());
    }

    @Test
    @DisplayName("Deve mapear conjunto de domínios para conjunto de entidades")
    void shouldMapToEntitySet() {
        var domain1 = WorkOrderLaborItem.restore(UUID.randomUUID(), UUID.randomUUID(), null, null, LaborExecutionStatus.AWAITING_EXECUTION);
        var domain2 = WorkOrderLaborItem.restore(UUID.randomUUID(), UUID.randomUUID(), null, null, LaborExecutionStatus.AWAITING_EXECUTION);

        var entities = WorkOrderLaborItemJpaMapper.toEntitySet(Set.of(domain1, domain2));

        assertNotNull(entities);
        assertEquals(2, entities.size());
    }

    @Test
    @DisplayName("Deve retornar null quando entidade for nula")
    void shouldReturnNullWhenEntityIsNull() {
        assertNull(WorkOrderLaborItemJpaMapper.toDomain(null));
    }

    @Test
    @DisplayName("Deve retornar null quando domínio for nulo")
    void shouldReturnNullWhenDomainIsNull() {
        assertNull(WorkOrderLaborItemJpaMapper.toEntity(null));
    }

    @Test
    @DisplayName("Deve retornar null quando conjunto de entidades for nulo")
    void shouldReturnNullWhenEntitiesSetIsNull() {
        assertNull(WorkOrderLaborItemJpaMapper.toDomainSet(null));
    }

    @Test
    @DisplayName("Deve retornar null quando conjunto de domínios for nulo")
    void shouldReturnNullWhenDomainsSetIsNull() {
        assertNull(WorkOrderLaborItemJpaMapper.toEntitySet(null));
    }
}
