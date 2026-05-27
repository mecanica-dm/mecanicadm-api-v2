package com.mecanicadm.mecanicadm_api.core.workorders.adapter.api.dto;

import com.mecanicadm.mecanicadm_api.core.workorders.domain.WorkOrder;
import com.mecanicadm.mecanicadm_api.core.workorders.domain.enums.WorkOrderStatus;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record WorkOrderResponse(
        UUID id,
        UUID clientId,
        String vehicleId,
        String description,
        WorkOrderStatus status,
        LocalDateTime executionStartAt,
        LocalDateTime executionEndAt,
        Long totalExecutionTimeInMinutes,
        List<WorkOrderLaborItemResponse> laborItems,
        List<WorkOrderMaterialItemResponse> materialItems,
        WorkOrderBudgetResponse budget,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public WorkOrderResponse(WorkOrder workOrder) {
        this(
                workOrder.getId(),
                workOrder.getClientId(),
                workOrder.getVehicleId(),
                workOrder.getDescription(),
                workOrder.getStatus(),
                workOrder.getExecutionStartAt().orElse(null),
                workOrder.getExecutionEndAt().orElse(null),
                calculateTotalExecutionTime(workOrder.getExecutionStartAt().orElse(null), workOrder.getExecutionEndAt().orElse(null)),
                workOrder.getLaborItems().stream().map(WorkOrderLaborItemResponse::new).toList(),
                workOrder.getMaterialItems().stream().map(WorkOrderMaterialItemResponse::new).toList(),
                workOrder.getBudget().map(WorkOrderBudgetResponse::new).orElse(null),
                workOrder.getDateCreated(),
                workOrder.getDateUpdated()
        );
    }

    private static Long calculateTotalExecutionTime(LocalDateTime start, LocalDateTime end) {
        if (start == null || end == null) {
            return 0L;
        }
        return Duration.between(start, end).toMinutes();
    }
}
