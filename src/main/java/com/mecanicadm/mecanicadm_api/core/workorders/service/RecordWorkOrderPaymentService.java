package com.mecanicadm.mecanicadm_api.core.workorders.service;

import com.mecanicadm.mecanicadm_api.core.workorders.adapter.repository.WorkOrderRepository;
import com.mecanicadm.mecanicadm_api.core.workorders.domain.WorkOrder;
import com.mecanicadm.mecanicadm_api.core.workorders.exception.WorkOrderExceptions;
import com.mecanicadm.mecanicadm_api.core.workorders.usecase.RecordWorkOrderPaymentUseCase;
import com.mecanicadm.mecanicadm_api.core.workorders.usecase.command.RecordWorkOrderPaymentCommand;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RecordWorkOrderPaymentService implements RecordWorkOrderPaymentUseCase {

    private final WorkOrderRepository workOrderRepository;

    public RecordWorkOrderPaymentService(WorkOrderRepository workOrderRepository) {
        this.workOrderRepository = workOrderRepository;
    }

    @Override
    @Transactional
    public void handle(RecordWorkOrderPaymentCommand cmd) {
        WorkOrder workOrder = workOrderRepository.findById(cmd.workOrderId())
                .orElseThrow(WorkOrderExceptions.NotFound::new);

        workOrder.markAsPaid();
        workOrderRepository.save(workOrder);
    }
}
