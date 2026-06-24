package com.mecanicadm.mecanicadm_api.core.workorder.domain.port;

import com.mecanicadm.mecanicadm_api.core.workorder.domain.port.WorkOrderExecutionDurationProjection;
import com.mecanicadm.mecanicadm_api.core.workorder.domain.port.WorkOrderExecutionSummaryProjection;
import com.mecanicadm.mecanicadm_api.core.workorder.domain.WorkOrder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

public interface WorkOrderGateway {
    WorkOrder create(WorkOrder workOrder);

    WorkOrder update(WorkOrder workOrder);

    Optional<WorkOrder> findById(UUID id);

    Optional<WorkOrder> findByIdWithItems(UUID id);

    WorkOrderPageResult findAll(WorkOrderPageQuery query);

    BigDecimal sumMaterialsTotalByWorkOrderId(UUID workOrderId);

    BigDecimal sumLaborTotalByWorkOrderId(UUID workOrderId);

    WorkOrderExecutionSummaryProjection getExecutionTimeSummary(
            LocalDateTime initialDateTime, LocalDateTime finalDateTimeExclusive
    );

    WorkOrderExecutionDurationProjection getSlowestExecution(
            LocalDateTime initialDateTime,
            LocalDateTime finalDateTimeExclusive
    );

    WorkOrderExecutionDurationProjection getFastestExecution(
            LocalDateTime initialDateTime,
            LocalDateTime finalDateTimeExclusive
    );

    Double getAverageLaborExecutionMinutes(
            LocalDateTime initialDateTime,
            LocalDateTime finalDateTimeExclusive
    );
}
