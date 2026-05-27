package com.mecanicadm.mecanicadm_api.core.workorders.service;

import com.mecanicadm.mecanicadm_api.core.workorders.adapter.repository.WorkOrderRepository;
import com.mecanicadm.mecanicadm_api.core.workorders.domain.WorkOrder;
import com.mecanicadm.mecanicadm_api.core.workorders.usecase.command.CreateWorkOrderCommand;
import org.junit.jupiter.api.BeforeEach;
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
class CreateWorkOrderServiceTest {

    @Mock
    private WorkOrderRepository repository;

    @InjectMocks
    private CreateWorkOrderService createWorkOrderService;

    private CreateWorkOrderCommand command;

    @BeforeEach
    void setUp() {
        command = new CreateWorkOrderCommand(
                UUID.randomUUID(),
                "ABC-1234",
                "Troca de pastilha de freio"
        );
    }

    @Test
    @DisplayName("Deve criar uma ordem de serviço com sucesso")
    void shouldCreateWorkOrderSuccessfully() {
        when(repository.save(any(WorkOrder.class))).thenAnswer(invocation -> invocation.getArgument(0));

        UUID resultId = createWorkOrderService.handle(command);

        assertNotNull(resultId);
        verify(repository).save(any(WorkOrder.class));
    }
}
