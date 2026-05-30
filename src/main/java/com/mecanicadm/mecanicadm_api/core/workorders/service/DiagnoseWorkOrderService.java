package com.mecanicadm.mecanicadm_api.core.workorders.service;

import com.mecanicadm.mecanicadm_api.core.client.adapter.repository.ClientRepository;
import com.mecanicadm.mecanicadm_api.core.client.exception.ClientExceptions;
import com.mecanicadm.mecanicadm_api.core.vehicle.domain.port.VehicleGateway;
import com.mecanicadm.mecanicadm_api.core.vehicle.exception.VehicleExceptions;
import com.mecanicadm.mecanicadm_api.core.workorders.adapter.repository.WorkOrderRepository;
import com.mecanicadm.mecanicadm_api.core.workorders.domain.WorkOrder;
import com.mecanicadm.mecanicadm_api.core.workorders.exception.WorkOrderExceptions;
import com.mecanicadm.mecanicadm_api.core.workorders.usecase.CalculateWorkOrderBudgetUseCase;
import com.mecanicadm.mecanicadm_api.core.workorders.usecase.DiagnoseWorkOrderUseCase;
import com.mecanicadm.mecanicadm_api.core.workorders.usecase.command.CalculateWorkOrderBudgetCommand;
import com.mecanicadm.mecanicadm_api.core.workorders.usecase.command.DiagnoseWorkOrderCommand;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.UUID;

@Service
public class DiagnoseWorkOrderService implements DiagnoseWorkOrderUseCase {

    private final WorkOrderRepository workOrderRepository;
    private final ClientRepository clientRepository;
    private final VehicleGateway vehicleRepository;
    private final CalculateWorkOrderBudgetUseCase calculateWorkOrderBudgetUseCase;

    public DiagnoseWorkOrderService(WorkOrderRepository workOrderRepository,
                                    ClientRepository clientRepository,
                                    VehicleGateway vehicleRepository,
                                    CalculateWorkOrderBudgetUseCase calculateWorkOrderBudgetUseCase) {
        this.workOrderRepository = workOrderRepository;
        this.clientRepository = clientRepository;
        this.vehicleRepository = vehicleRepository;
        this.calculateWorkOrderBudgetUseCase = calculateWorkOrderBudgetUseCase;
    }

    @Override
    @Transactional
    public UUID handle(DiagnoseWorkOrderCommand cmd) {
        WorkOrder workOrder = workOrderRepository.findById(cmd.workOrderId())
                .orElseThrow(WorkOrderExceptions.NotFound::new);

        validateDiagnosisPreconditions(workOrder);

        workOrder.markAsDiagnosed();

        calculateWorkOrderBudgetUseCase.handle(new CalculateWorkOrderBudgetCommand(workOrder.getId()));
        return workOrder.getId();
    }

    private void validateDiagnosisPreconditions(WorkOrder workOrder) {
        if (workOrder.getLaborItems().isEmpty()) {
            throw new WorkOrderExceptions.LaborItemsRequired();
        }

        if (Objects.isNull(workOrder.getClientId())) {
            throw new WorkOrderExceptions.ClientRequired();
        }

        if (Objects.isNull(workOrder.getVehicleId()) || workOrder.getVehicleId().isBlank()) {
            throw new WorkOrderExceptions.VehicleRequired();
        }

        if (!clientRepository.existsById(workOrder.getClientId())) {
            throw new ClientExceptions.NotFound();
        }

        if (!vehicleRepository.existsByLicensePlate(workOrder.getVehicleId())) {
            throw new VehicleExceptions.NotFound();
        }
    }
}
