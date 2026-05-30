package com.mecanicadm.mecanicadm_api.core.stockmovements.service;

import com.mecanicadm.mecanicadm_api.core.shared.exception.DomainExceptionCore;
import com.mecanicadm.mecanicadm_api.core.stockmovements.adapter.repository.StockMovementsRepository;
import com.mecanicadm.mecanicadm_api.core.stockmovements.domain.StockMovements;
import com.mecanicadm.mecanicadm_api.core.stockmovements.exception.StockMovementsExceptions;
import com.mecanicadm.mecanicadm_api.core.stockmovements.usecase.command.AddStockCommand;
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
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class AddStockServiceTest {

    @Mock
    private StockMovementsRepository stockMovementsRepository;

    @InjectMocks
    private AddStockService addStockService;

    @Test
    @DisplayName("Deve registrar adicao de estoque como novo movimento")
    void shouldRecordAdditionAsNewMovement() {
        UUID materialId = UUID.randomUUID();

        addStockService.handle(new AddStockCommand(materialId, 10));

        verify(stockMovementsRepository).save(any(StockMovements.class));
    }

    @Test
    @DisplayName("Nao deve adicionar estoque com quantidade invalida")
    void shouldNotAddStockWithInvalidQuantity() {
        UUID materialId = UUID.randomUUID();

        AddStockCommand command = new AddStockCommand(materialId, 0);

        DomainExceptionCore exception = assertThrows(
                DomainExceptionCore.class,
                () -> addStockService.handle(command)
        );

        assertAll(
                () -> assertInstanceOf(StockMovementsExceptions.InvalidQuantity.class, exception),
                () -> assertEquals("stock.quantity.invalid", exception.getMessageKey()),
                () -> assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus())
        );
        verify(stockMovementsRepository, never()).save(any());
    }
}
