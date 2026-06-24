package com.mecanicadm.mecanicadm_api.core.stockmovements.usecase;

import com.mecanicadm.mecanicadm_api.core.stockmovements.domain.StockMovements;
import com.mecanicadm.mecanicadm_api.core.stockmovements.domain.port.StockMovementsGateway;
import com.mecanicadm.mecanicadm_api.core.stockmovements.exception.StockMovementsExceptions;
import com.mecanicadm.mecanicadm_api.core.stockmovements.usecase.command.SoftDeleteStockCommand;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SoftDeleteStockUseCaseTest {

    @Mock
    private StockMovementsGateway gateway;

    private SoftDeleteStockUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new SoftDeleteStockUseCase(gateway);
    }

    @Test
    @DisplayName("Deve realizar soft delete do movimento de estoque com sucesso")
    void shouldSoftDeleteStockSuccessfully() {
        var materialId = UUID.randomUUID();
        var workOrderId = UUID.randomUUID();
        var command = new SoftDeleteStockCommand(materialId, workOrderId);
        var stock = mock(StockMovements.class);
        when(gateway.findByMaterialIdAndWorkOrderId(materialId, workOrderId)).thenReturn(Optional.of(stock));

        useCase.execute(command);

        verify(gateway).findByMaterialIdAndWorkOrderId(materialId, workOrderId);
        verify(stock).delete();
        verify(gateway).update(stock);
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar excluir movimento inexistente")
    void shouldThrowExceptionWhenStockNotFound() {
        var materialId = UUID.randomUUID();
        var workOrderId = UUID.randomUUID();
        var command = new SoftDeleteStockCommand(materialId, workOrderId);
        when(gateway.findByMaterialIdAndWorkOrderId(materialId, workOrderId)).thenReturn(Optional.empty());

        assertThrows(StockMovementsExceptions.NotFound.class, () -> useCase.execute(command));

        verify(gateway).findByMaterialIdAndWorkOrderId(materialId, workOrderId);
        verify(gateway, never()).update(any());
    }
}
