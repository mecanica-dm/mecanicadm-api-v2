package com.mecanicadm.mecanicadm_api.core.workorders.service;

import com.mecanicadm.mecanicadm_api.core.material.domain.Material;
import com.mecanicadm.mecanicadm_api.core.material.domain.port.MaterialGateway;
import com.mecanicadm.mecanicadm_api.core.material.exception.MaterialExceptions;
import com.mecanicadm.mecanicadm_api.core.stockmovements.usecase.DeductStockUseCase;
import com.mecanicadm.mecanicadm_api.core.stockmovements.usecase.command.DeductStockCommand;
import com.mecanicadm.mecanicadm_api.core.workorders.domain.WorkOrderMaterialItem;
import com.mecanicadm.mecanicadm_api.core.workorders.usecase.command.CreateWorkOrderMaterialItemCommand;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateWorkOrderMaterialItemServiceTest {

    @Mock
    private MaterialGateway materialGateway;

    @Mock
    private DeductStockUseCase deductStockUseCase;

    @InjectMocks
    private CreateWorkOrderMaterialItemService createWorkOrderMaterialItemService;

    private CreateWorkOrderMaterialItemCommand command;
    private UUID workOrderId;
    private UUID materialId;
    private int quantity;

    @BeforeEach
    void setUp() {
        workOrderId = UUID.randomUUID();
        materialId = UUID.randomUUID();
        quantity = 5;
        command = new CreateWorkOrderMaterialItemCommand(workOrderId, materialId, quantity);
    }

    @Test
    @DisplayName("Deve criar um item de material da ordem de serviço com sucesso")
    void shouldCreateWorkOrderMaterialItemSuccessfully() {
        Material material = mock(Material.class);
        when(material.getId()).thenReturn(materialId);
        when(materialGateway.findById(materialId)).thenReturn(Optional.of(material));

        WorkOrderMaterialItem result = createWorkOrderMaterialItemService.handle(command);

        assertNotNull(result);
        assertEquals(materialId, result.getMaterialId());
        assertEquals(quantity, result.getQuantity());
        verify(materialGateway).findById(materialId);
        verify(deductStockUseCase).execute(new DeductStockCommand(materialId, workOrderId, quantity));
    }

    @Test
    @DisplayName("Deve lançar exceção quando o material não for encontrado")
    void shouldThrowExceptionWhenMaterialNotFound() {
        when(materialGateway.findById(materialId)).thenReturn(Optional.empty());

        assertThrows(MaterialExceptions.MaterialNotFound.class, () -> createWorkOrderMaterialItemService.handle(command));
        verify(materialGateway).findById(materialId);
        verifyNoInteractions(deductStockUseCase);
    }
}
