package com.mecanicadm.mecanicadm_api.infra.features.workorder.persistence.jpa;

import com.mecanicadm.mecanicadm_api.core.workorder.domain.WorkOrderMaterialItem;
import com.mecanicadm.mecanicadm_api.infra.features.workorder.persistence.entity.WorkOrderMaterialItemJpaEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class WorkOrderMaterialItemJpaMapperTest {

    @Test
    @DisplayName("Deve mapear JpaEntity para domínio")
    void shouldMapToDomain() {
        var id = UUID.randomUUID();
        var materialId = UUID.randomUUID();
        var entity = new WorkOrderMaterialItemJpaEntity(id, materialId, 5);

        var domain = WorkOrderMaterialItemJpaMapper.toDomain(entity);

        assertEquals(id, domain.getId());
        assertEquals(materialId, domain.getMaterialId());
        assertEquals(5, domain.getQuantity());
    }

    @Test
    @DisplayName("Deve mapear domínio para JpaEntity")
    void shouldMapToEntity() {
        var id = UUID.randomUUID();
        var materialId = UUID.randomUUID();
        var domain = WorkOrderMaterialItem.restore(id, materialId, 10);

        var entity = WorkOrderMaterialItemJpaMapper.toEntity(domain);

        assertEquals(id, entity.getId());
        assertEquals(materialId, entity.getMaterialId());
        assertEquals(10, entity.getQuantity());
    }

    @Test
    @DisplayName("Deve mapear conjunto de entidades para conjunto de domínios")
    void shouldMapToDomainSet() {
        var entity1 = new WorkOrderMaterialItemJpaEntity(UUID.randomUUID(), UUID.randomUUID(), 1);
        var entity2 = new WorkOrderMaterialItemJpaEntity(UUID.randomUUID(), UUID.randomUUID(), 2);

        var domains = WorkOrderMaterialItemJpaMapper.toDomainSet(Set.of(entity1, entity2));

        assertNotNull(domains);
        assertEquals(2, domains.size());
    }

    @Test
    @DisplayName("Deve mapear conjunto de domínios para conjunto de entidades")
    void shouldMapToEntitySet() {
        var domain1 = WorkOrderMaterialItem.restore(UUID.randomUUID(), UUID.randomUUID(), 3);
        var domain2 = WorkOrderMaterialItem.restore(UUID.randomUUID(), UUID.randomUUID(), 4);

        var entities = WorkOrderMaterialItemJpaMapper.toEntitySet(Set.of(domain1, domain2));

        assertNotNull(entities);
        assertEquals(2, entities.size());
    }

    @Test
    @DisplayName("Deve retornar null quando entidade for nula")
    void shouldReturnNullWhenEntityIsNull() {
        assertNull(WorkOrderMaterialItemJpaMapper.toDomain(null));
    }

    @Test
    @DisplayName("Deve retornar null quando domínio for nulo")
    void shouldReturnNullWhenDomainIsNull() {
        assertNull(WorkOrderMaterialItemJpaMapper.toEntity(null));
    }

    @Test
    @DisplayName("Deve retornar null quando conjunto de entidades for nulo")
    void shouldReturnNullWhenEntitiesSetIsNull() {
        assertNull(WorkOrderMaterialItemJpaMapper.toDomainSet(null));
    }

    @Test
    @DisplayName("Deve retornar null quando conjunto de domínios for nulo")
    void shouldReturnNullWhenDomainsSetIsNull() {
        assertNull(WorkOrderMaterialItemJpaMapper.toEntitySet(null));
    }
}
