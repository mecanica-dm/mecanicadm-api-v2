package com.mecanicadm.mecanicadm_api.core.workorders.service;

import com.mecanicadm.mecanicadm_api.core.client.exception.ClientExceptions;
import com.mecanicadm.mecanicadm_api.core.vehicle.domain.port.VehicleGateway;
import com.mecanicadm.mecanicadm_api.core.vehicle.exception.VehicleExceptions;
import com.mecanicadm.mecanicadm_api.core.workorders.domain.WorkOrder;
import com.mecanicadm.mecanicadm_api.core.workorders.domain.WorkOrderLaborItem;
import com.mecanicadm.mecanicadm_api.core.workorders.domain.WorkOrderMaterialItem;
import com.mecanicadm.mecanicadm_api.core.workorders.domain.enums.WorkOrderStatus;
import com.mecanicadm.mecanicadm_api.core.workorders.exception.WorkOrderExceptions;
import com.mecanicadm.mecanicadm_api.core.workorders.usecase.CalculateWorkOrderBudgetUseCase;
import com.mecanicadm.mecanicadm_api.core.workorders.usecase.command.CalculateWorkOrderBudgetCommand;
import com.mecanicadm.mecanicadm_api.core.workorders.usecase.command.DiagnoseWorkOrderCommand;
import com.mecanicadm.mecanicadm_api.infra.features.client.persistence.jpa.ClientJpaRepository;
import com.mecanicadm.mecanicadm_api.infra.features.workorder.persistence.jpa.WorkOrderJpaRepository;
import com.mecanicadm.mecanicadm_api.shared.exception.DomainExceptionCore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DiagnoseWorkOrderServiceTest {

    @Mock
    private WorkOrderJpaRepository workOrderRepository;

    @Mock
    private ClientJpaRepository clientRepository;

    @Mock
    private VehicleGateway vehicleRepository;

    @Mock
    private CalculateWorkOrderBudgetUseCase calculateWorkOrderBudgetUseCase;

    private DiagnoseWorkOrderService service;

    private WorkOrder workOrder;
    private UUID workOrderId;

    @BeforeEach
    void setUp() {
        service = new DiagnoseWorkOrderService(workOrderRepository, clientRepository, vehicleRepository, calculateWorkOrderBudgetUseCase);
        workOrder = WorkOrder.create(UUID.randomUUID(), "ABC-1234", "Diagnostico eletrico");
        workOrderId = workOrder.getId();
    }

    @Test
    @DisplayName("Deve diagnosticar ordem de servico quando os pre-requisitos forem atendidos")
    void shouldDiagnoseWorkOrderWhenPreconditionsAreMet() {
        UUID materialId = UUID.randomUUID();
        ReflectionTestUtils.setField(workOrder, "laborItems", Set.of(WorkOrderLaborItem.create(UUID.randomUUID())));
        ReflectionTestUtils.setField(workOrder, "materialItems", Set.of(WorkOrderMaterialItem.create(materialId, 2)));

        when(workOrderRepository.findById(workOrderId)).thenReturn(Optional.of(workOrder));
        when(clientRepository.existsById(workOrder.getClientId())).thenReturn(true);
        when(vehicleRepository.existsByLicensePlate(workOrder.getVehicleId())).thenReturn(true);

        UUID result = service.handle(new DiagnoseWorkOrderCommand(workOrderId));

        assertAll(
                () -> assertEquals(workOrderId, result),
                () -> assertEquals(WorkOrderStatus.DIAGNOSED, workOrder.getStatus())
        );
        verify(calculateWorkOrderBudgetUseCase).handle(new CalculateWorkOrderBudgetCommand(workOrderId));
    }

    @Test
    @DisplayName("Deve impedir diagnostico sem itens de servico")
    void shouldThrowLaborItemsRequiredWhenWorkOrderHasNoLaborItems() {
        when(workOrderRepository.findById(workOrderId)).thenReturn(Optional.of(workOrder));

        DiagnoseWorkOrderCommand command =
                new DiagnoseWorkOrderCommand(workOrderId);

        DomainExceptionCore exception = assertThrows(
                DomainExceptionCore.class,
                () -> service.handle(command)
        );

        assertAll(
                () -> assertInstanceOf(WorkOrderExceptions.LaborItemsRequired.class, exception),
                () -> assertEquals("work.order.labor.required", exception.getMessageKey()),
                () -> assertEquals(400, exception.getStatus())
        );
        verifyNoInteractions(clientRepository, vehicleRepository, calculateWorkOrderBudgetUseCase);
    }

    @Test
    @DisplayName("Deve impedir diagnostico quando cliente nao estiver informado")
    void shouldThrowClientRequiredWhenWorkOrderHasNoClient() {
        ReflectionTestUtils.setField(workOrder, "laborItems", Set.of(WorkOrderLaborItem.create(UUID.randomUUID())));
        ReflectionTestUtils.setField(workOrder, "clientId", null);
        when(workOrderRepository.findById(workOrderId)).thenReturn(Optional.of(workOrder));

        DiagnoseWorkOrderCommand command =
                new DiagnoseWorkOrderCommand(workOrderId);

        DomainExceptionCore exception = assertThrows(
                DomainExceptionCore.class,
                () -> service.handle(command)
        );

        assertAll(
                () -> assertInstanceOf(WorkOrderExceptions.ClientRequired.class, exception),
                () -> assertEquals("work.order.client.required", exception.getMessageKey()),
                () -> assertEquals(400, exception.getStatus())
        );
        verifyNoInteractions(clientRepository, vehicleRepository, calculateWorkOrderBudgetUseCase);
    }

    @Test
    @DisplayName("Deve impedir diagnostico quando veiculo nao estiver informado")
    void shouldThrowVehicleRequiredWhenWorkOrderHasNoVehicle() {
        ReflectionTestUtils.setField(workOrder, "laborItems", Set.of(WorkOrderLaborItem.create(UUID.randomUUID())));
        ReflectionTestUtils.setField(workOrder, "vehicleId", "   ");
        when(workOrderRepository.findById(workOrderId)).thenReturn(Optional.of(workOrder));

        DiagnoseWorkOrderCommand command =
                new DiagnoseWorkOrderCommand(workOrderId);

        DomainExceptionCore exception = assertThrows(
                DomainExceptionCore.class,
                () -> service.handle(command)
        );

        assertAll(
                () -> assertInstanceOf(WorkOrderExceptions.VehicleRequired.class, exception),
                () -> assertEquals("work.order.vehicle.required", exception.getMessageKey()),
                () -> assertEquals(400, exception.getStatus())
        );
        verifyNoInteractions(clientRepository, vehicleRepository, calculateWorkOrderBudgetUseCase);
    }

    @Test
    @DisplayName("Deve impedir diagnostico quando veiculo estiver nulo")
    void shouldThrowVehicleRequiredWhenWorkOrderVehicleIsNull() {
        ReflectionTestUtils.setField(workOrder, "laborItems", Set.of(WorkOrderLaborItem.create(UUID.randomUUID())));
        ReflectionTestUtils.setField(workOrder, "vehicleId", null);
        when(workOrderRepository.findById(workOrderId)).thenReturn(Optional.of(workOrder));

        DiagnoseWorkOrderCommand command =
                new DiagnoseWorkOrderCommand(workOrderId);

        DomainExceptionCore exception = assertThrows(
                DomainExceptionCore.class,
                () -> service.handle(command)
        );

        assertAll(
                () -> assertInstanceOf(WorkOrderExceptions.VehicleRequired.class, exception),
                () -> assertEquals("work.order.vehicle.required", exception.getMessageKey()),
                () -> assertEquals(400, exception.getStatus())
        );
        verifyNoInteractions(clientRepository, vehicleRepository, calculateWorkOrderBudgetUseCase);
    }

    @Test
    @DisplayName("Deve impedir diagnostico quando cliente nao existir")
    void shouldThrowClientNotFoundWhenClientDoesNotExist() {
        ReflectionTestUtils.setField(workOrder, "laborItems", Set.of(WorkOrderLaborItem.create(UUID.randomUUID())));
        when(workOrderRepository.findById(workOrderId)).thenReturn(Optional.of(workOrder));
        when(clientRepository.existsById(workOrder.getClientId())).thenReturn(false);

        DiagnoseWorkOrderCommand command =
                new DiagnoseWorkOrderCommand(workOrderId);

        DomainExceptionCore exception = assertThrows(
                DomainExceptionCore.class,
                () -> service.handle(command)
        );

        assertAll(
                () -> assertInstanceOf(ClientExceptions.NotFound.class, exception),
                () -> assertEquals("client.not.found", exception.getMessageKey()),
                () -> assertEquals(404, exception.getStatus())
        );
        verify(vehicleRepository, never()).existsByLicensePlate(any());
        verifyNoInteractions(calculateWorkOrderBudgetUseCase);
    }

    @Test
    @DisplayName("Deve impedir diagnostico quando veiculo nao existir")
    void shouldThrowVehicleNotFoundWhenVehicleDoesNotExist() {
        ReflectionTestUtils.setField(workOrder, "laborItems", Set.of(WorkOrderLaborItem.create(UUID.randomUUID())));
        when(workOrderRepository.findById(workOrderId)).thenReturn(Optional.of(workOrder));
        when(clientRepository.existsById(workOrder.getClientId())).thenReturn(true);
        when(vehicleRepository.existsByLicensePlate(workOrder.getVehicleId())).thenReturn(false);

        DiagnoseWorkOrderCommand command =
                new DiagnoseWorkOrderCommand(workOrderId);

        DomainExceptionCore exception = assertThrows(
                DomainExceptionCore.class,
                () -> service.handle(command)
        );

        assertAll(
                () -> assertInstanceOf(VehicleExceptions.NotFound.class, exception),
                () -> assertEquals("vehicle.not.found", exception.getMessageKey()),
                () -> assertEquals(404, exception.getStatus())
        );
        verifyNoInteractions(calculateWorkOrderBudgetUseCase);
    }

    @Test
    @DisplayName("Deve lancar excecao quando ordem de servico nao existir")
    void shouldThrowWorkOrderNotFoundWhenWorkOrderDoesNotExist() {
        when(workOrderRepository.findById(workOrderId)).thenReturn(Optional.empty());

        DiagnoseWorkOrderCommand command =
                new DiagnoseWorkOrderCommand(workOrderId);

        DomainExceptionCore exception = assertThrows(
                DomainExceptionCore.class,
                () -> service.handle(command)
        );

        assertAll(
                () -> assertInstanceOf(WorkOrderExceptions.NotFound.class, exception),
                () -> assertEquals("work.order.not.found", exception.getMessageKey()),
                () -> assertEquals(404, exception.getStatus())
        );
        verifyNoInteractions(clientRepository, vehicleRepository, calculateWorkOrderBudgetUseCase);
    }
}
