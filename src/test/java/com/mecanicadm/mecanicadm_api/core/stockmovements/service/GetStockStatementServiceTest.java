package com.mecanicadm.mecanicadm_api.core.stockmovements.service;

import com.mecanicadm.mecanicadm_api.core.stockmovements.adapter.repository.StockMovementsRepository;
import com.mecanicadm.mecanicadm_api.core.stockmovements.domain.StockMovements;
import com.mecanicadm.mecanicadm_api.core.stockmovements.usecase.dto.StockStatementDTO;
import com.mecanicadm.mecanicadm_api.core.stockmovements.usecase.query.GetStockStatementQuery;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

class GetStockStatementServiceTest {

    @Mock
    private StockMovementsRepository stockMovementsRepository;

    @InjectMocks
    private GetStockStatementService getStockStatementService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Deve retornar o extrato de estoque corretamente")
    void shouldReturnStockStatementCorrectly() {
        UUID materialId = UUID.randomUUID();
        GetStockStatementQuery query = new GetStockStatementQuery(materialId);

        StockMovements addition = StockMovements.recordAddition(materialId, 10);
        StockMovements reduction = StockMovements.recordReduction(materialId, UUID.randomUUID(), 3);

        when(stockMovementsRepository.getCurrentBalanceByMaterialId(materialId)).thenReturn(7);
        when(stockMovementsRepository.findAllByMaterialIdOrderByDateCreatedDesc(materialId))
                .thenReturn(List.of(addition, reduction));

        StockStatementDTO result = getStockStatementService.handle(query);

        assertNotNull(result);
        assertEquals(materialId, result.materialId());
        assertEquals(7, result.currentBalance());
        assertEquals(2, result.movements().size());
    }
}
