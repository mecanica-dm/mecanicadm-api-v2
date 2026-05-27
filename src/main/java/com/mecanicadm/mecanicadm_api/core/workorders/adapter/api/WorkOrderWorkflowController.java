package com.mecanicadm.mecanicadm_api.core.workorders.adapter.api;

import com.mecanicadm.mecanicadm_api.core.workorders.adapter.api.openapi.WorkOrderWorkflowOpenApi;
import com.mecanicadm.mecanicadm_api.core.workorders.usecase.*;
import com.mecanicadm.mecanicadm_api.core.workorders.usecase.command.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/work-orders/{id}/step/")
public class WorkOrderWorkflowController implements WorkOrderWorkflowOpenApi {

    private final DiagnoseWorkOrderUseCase diagnoseWorkOrderUseCase;
    private final StartWorkOrderExecutionUseCase startWorkOrderExecutionUseCase;
    private final FinishWorkOrderExecutionUseCase finishWorkOrderExecutionUseCase;
    private final RecordWorkOrderPaymentUseCase recordWorkOrderPaymentUseCase;
    private final DeliverWorkOrderUseCase deliverWorkOrderUseCase;

    public WorkOrderWorkflowController(DiagnoseWorkOrderUseCase diagnoseWorkOrderUseCase,
                                       StartWorkOrderExecutionUseCase startWorkOrderExecutionUseCase,
                                       FinishWorkOrderExecutionUseCase finishWorkOrderExecutionUseCase,
                                       RecordWorkOrderPaymentUseCase recordWorkOrderPaymentUseCase,
                                       DeliverWorkOrderUseCase deliverWorkOrderUseCase) {
        this.diagnoseWorkOrderUseCase = diagnoseWorkOrderUseCase;
        this.startWorkOrderExecutionUseCase = startWorkOrderExecutionUseCase;
        this.finishWorkOrderExecutionUseCase = finishWorkOrderExecutionUseCase;
        this.recordWorkOrderPaymentUseCase = recordWorkOrderPaymentUseCase;
        this.deliverWorkOrderUseCase = deliverWorkOrderUseCase;
    }

    @Override
    @PostMapping("/diagnose")
    public ResponseEntity<UUID> diagnose(@PathVariable UUID id) {
        UUID workOrderId = diagnoseWorkOrderUseCase.handle(new DiagnoseWorkOrderCommand(id));
        return ResponseEntity.ok(workOrderId);
    }

    @Override
    @PostMapping("/start-execution")
    public ResponseEntity<Void> startExecution(@PathVariable UUID id) {
        startWorkOrderExecutionUseCase.handle(new StartWorkOrderExecutionCommand(id));
        return ResponseEntity.noContent().build();
    }

    @Override
    @PostMapping("/finish-execution")
    public ResponseEntity<Void> finishExecution(@PathVariable UUID id) {
        finishWorkOrderExecutionUseCase.handle(new FinishWorkOrderExecutionCommand(id));
        return ResponseEntity.noContent().build();
    }

    @Override
    @PostMapping("/payment")
    public ResponseEntity<Void> recordPayment(@PathVariable UUID id) {
        recordWorkOrderPaymentUseCase.handle(new RecordWorkOrderPaymentCommand(id));
        return ResponseEntity.noContent().build();
    }

    @Override
    @PostMapping("/deliver")
    public ResponseEntity<Void> deliver(@PathVariable UUID id) {
        deliverWorkOrderUseCase.handle(new DeliverWorkOrderCommand(id));
        return ResponseEntity.noContent().build();
    }
}

