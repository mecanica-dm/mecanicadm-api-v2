package com.mecanicadm.mecanicadm_api.infra.features.workorder.persistence.jpa;

import com.mecanicadm.mecanicadm_api.core.workorder.domain.WorkOrderLaborItem;
import com.mecanicadm.mecanicadm_api.infra.features.workorder.persistence.entity.WorkOrderLaborItemJpaEntity;
import com.mecanicadm.mecanicadm_api.shared.exception.TechnicalException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WorkOrderLaborItemRepositoryImplTest {

    @Mock
    private WorkOrderLaborItemJpaRepository jpaRepository;

    private WorkOrderLaborItemRepositoryImpl repository;
    private UUID id;
    private WorkOrderLaborItem laborItem;
    private WorkOrderLaborItemJpaEntity entity;

    @BeforeEach
    void setUp() {
        repository = new WorkOrderLaborItemRepositoryImpl(jpaRepository);

        id = UUID.randomUUID();
        laborItem = mock(WorkOrderLaborItem.class);
        lenient().when(laborItem.getId()).thenReturn(id);

        entity = mock(WorkOrderLaborItemJpaEntity.class);
    }

    @Test
    @DisplayName("Deve buscar labor item por ID com sucesso")
    void shouldFindLaborItemById() {
        when(jpaRepository.findById(id)).thenReturn(Optional.of(entity));

        try (MockedStatic<WorkOrderLaborItemJpaMapper> mapper = mockStatic(WorkOrderLaborItemJpaMapper.class)) {
            mapper.when(() -> WorkOrderLaborItemJpaMapper.toDomain(entity)).thenReturn(laborItem);

            Optional<WorkOrderLaborItem> result = repository.findById(id);

            assertTrue(result.isPresent());
            assertSame(laborItem, result.get());
            verify(jpaRepository).findById(id);
        }
    }

    @Test
    @DisplayName("Deve retornar vazio ao buscar labor item por ID inexistente")
    void shouldReturnEmptyWhenLaborItemNotFoundById() {
        when(jpaRepository.findById(id)).thenReturn(Optional.empty());

        Optional<WorkOrderLaborItem> result = repository.findById(id);

        assertTrue(result.isEmpty());
        verify(jpaRepository).findById(id);
    }

    @Test
    @DisplayName("Deve deletar labor item com sucesso")
    void shouldDeleteLaborItemSuccessfully() {
        try (MockedStatic<WorkOrderLaborItemJpaMapper> mapper = mockStatic(WorkOrderLaborItemJpaMapper.class)) {
            mapper.when(() -> WorkOrderLaborItemJpaMapper.toEntity(laborItem)).thenReturn(entity);

            repository.delete(laborItem);

            verify(jpaRepository).delete(entity);
        }
    }

    @Test
    @DisplayName("Deve lancar excecao ao deletar labor item nulo")
    void shouldThrowExceptionWhenDeletingNullLaborItem() {
        assertThrows(TechnicalException.class, () -> repository.delete(null));
        verifyNoInteractions(jpaRepository);
    }
}
