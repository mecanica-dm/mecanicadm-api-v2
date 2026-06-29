package com.mecanicadm.mecanicadm_api.core.workorder.usecase;

import com.mecanicadm.mecanicadm_api.core.workorder.domain.port.WorkOrderGateway;
import com.mecanicadm.mecanicadm_api.core.workorder.usecase.command.CreateWorkOrderCommand;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateWorkOrderUseCaseTest {

    @Mock
    private WorkOrderGateway gateway;

    @InjectMocks
    private CreateWorkOrderUseCase useCase;

    @Test
    @DisplayName("Deve criar uma ordem de serviço com sucesso")
    void shouldCreateWorkOrderSuccessfully() {
        UUID clientId = UUID.randomUUID();
        String vehicleId = "ABC-1234";
        String description = "Troca de oleo";
        CreateWorkOrderCommand command = new CreateWorkOrderCommand(clientId, vehicleId, description);

        when(gateway.create(any())).thenAnswer(invocation -> invocation.getArgument(0));

        UUID resultId = useCase.execute(command);

        assertNotNull(resultId);
        verify(gateway).create(any());
    }
}
