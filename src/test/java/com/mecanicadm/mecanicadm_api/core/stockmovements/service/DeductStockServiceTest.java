package com.mecanicadm.mecanicadm_api.core.stockmovements.service;

import com.mecanicadm.mecanicadm_api.core.stockmovements.adapter.repository.StockMovementsRepository;
import com.mecanicadm.mecanicadm_api.core.stockmovements.domain.StockMovements;
import com.mecanicadm.mecanicadm_api.core.stockmovements.exception.StockMovementsExceptions;
import com.mecanicadm.mecanicadm_api.core.stockmovements.usecase.command.DeductStockCommand;
import com.mecanicadm.mecanicadm_api.infra.exception.DomainException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeductStockServiceTest {

    @Mock
    private StockMovementsRepository stockMovementsRepository;

    @InjectMocks
    private DeductStockService deductStockService;

    @Test
    @DisplayName("Deve registrar deducao como novo movimento quando houver saldo")
    void shouldRecordReductionAsNewMovementWhenHasBalance() {
        UUID materialId = UUID.randomUUID();
        UUID workOrderId = UUID.randomUUID();
        when(stockMovementsRepository.getCurrentBalanceByMaterialId(materialId)).thenReturn(20);

        deductStockService.handle(new DeductStockCommand(materialId, workOrderId, 5));

        verify(stockMovementsRepository).save(any(StockMovements.class));
    }

    @Test
    @DisplayName("Nao deve deduzir quando quantidade for invalida")
    void shouldNotDeductWithInvalidQuantity() {
        UUID materialId = UUID.randomUUID();
        UUID workOrderId = UUID.randomUUID();

        DeductStockCommand command = new DeductStockCommand(materialId, workOrderId, 0);

        DomainException exception = assertThrows(
                DomainException.class,
                () -> deductStockService.handle(command)
        );

        assertAll(
                () -> assertInstanceOf(StockMovementsExceptions.InvalidQuantity.class, exception),
                () -> assertEquals("stock.quantity.invalid", exception.getMessageKey()),
                () -> assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus())
        );
        verify(stockMovementsRepository, never()).save(any());
    }

    @Test
    @DisplayName("Nao deve deduzir quando saldo for insuficiente")
    void shouldNotDeductWhenStockIsInsufficient() {
        UUID materialId = UUID.randomUUID();
        UUID workOrderId = UUID.randomUUID();
        when(stockMovementsRepository.getCurrentBalanceByMaterialId(materialId)).thenReturn(3);

        DeductStockCommand command = new DeductStockCommand(materialId, workOrderId, 5);

        DomainException exception = assertThrows(
                DomainException.class,
                () -> deductStockService.handle(command)
        );

        assertAll(
                () -> assertInstanceOf(StockMovementsExceptions.InsufficientStock.class, exception),
                () -> assertEquals("stock.quantity.insufficient", exception.getMessageKey()),
                () -> assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus())
        );
        verify(stockMovementsRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve consultar saldo antes de deduzir")
    void shouldQueryCurrentBalanceBeforeDeduction() {
        UUID materialId = UUID.randomUUID();
        UUID workOrderId = UUID.randomUUID();
        when(stockMovementsRepository.getCurrentBalanceByMaterialId(materialId)).thenReturn(10);

        deductStockService.handle(new DeductStockCommand(materialId, workOrderId, 5));

        verify(stockMovementsRepository).getCurrentBalanceByMaterialId(materialId);
        verify(stockMovementsRepository).save(any(StockMovements.class));
    }
}
