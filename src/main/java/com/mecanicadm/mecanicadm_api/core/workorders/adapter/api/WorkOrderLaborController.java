package com.mecanicadm.mecanicadm_api.core.workorders.adapter.api;

import com.mecanicadm.mecanicadm_api.core.workorders.adapter.api.dto.WorkOrderLaborItemResponse;
import com.mecanicadm.mecanicadm_api.core.workorders.adapter.api.openapi.WorkOrderLaborOpenApi;
import com.mecanicadm.mecanicadm_api.core.workorders.domain.WorkOrderLaborItem;
import com.mecanicadm.mecanicadm_api.core.workorders.usecase.AddLaborToWorkOrderUseCase;
import com.mecanicadm.mecanicadm_api.core.workorders.usecase.FinishLaborExecutionUseCase;
import com.mecanicadm.mecanicadm_api.core.workorders.usecase.GetWorkOrderLaborItemByIdUseCase;
import com.mecanicadm.mecanicadm_api.core.workorders.usecase.RemoveLaborItemFromWorkOrderUseCase;
import com.mecanicadm.mecanicadm_api.core.workorders.usecase.StartLaborExecutionUseCase;
import com.mecanicadm.mecanicadm_api.core.workorders.usecase.command.AddLaborToWorkOrderCommand;
import com.mecanicadm.mecanicadm_api.core.workorders.usecase.command.FinishLaborExecutionCommand;
import com.mecanicadm.mecanicadm_api.core.workorders.usecase.command.RemoveLaborItemFromWorkOrderCommand;
import com.mecanicadm.mecanicadm_api.core.workorders.usecase.command.StartLaborExecutionCommand;
import com.mecanicadm.mecanicadm_api.core.workorders.usecase.query.GetWorkOrderLaborItemByIdQuery;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/work-orders/{workOrderId}/labors")
public class WorkOrderLaborController implements WorkOrderLaborOpenApi {

    private final StartLaborExecutionUseCase startLaborExecutionUseCase;
    private final FinishLaborExecutionUseCase finishLaborExecutionUseCase;
    private final GetWorkOrderLaborItemByIdUseCase getWorkOrderLaborItemByIdUseCase;
    private final AddLaborToWorkOrderUseCase addLaborToWorkOrderUseCase;
    private final RemoveLaborItemFromWorkOrderUseCase removeLaborItemFromWorkOrderUseCase;

    public WorkOrderLaborController(StartLaborExecutionUseCase startLaborExecutionUseCase,
                                    FinishLaborExecutionUseCase finishLaborExecutionUseCase,
                                    GetWorkOrderLaborItemByIdUseCase getWorkOrderLaborItemByIdUseCase,
                                    AddLaborToWorkOrderUseCase addLaborToWorkOrderUseCase,
                                    RemoveLaborItemFromWorkOrderUseCase removeLaborItemFromWorkOrderUseCase) {
        this.startLaborExecutionUseCase = startLaborExecutionUseCase;
        this.finishLaborExecutionUseCase = finishLaborExecutionUseCase;
        this.getWorkOrderLaborItemByIdUseCase = getWorkOrderLaborItemByIdUseCase;
        this.addLaborToWorkOrderUseCase = addLaborToWorkOrderUseCase;
        this.removeLaborItemFromWorkOrderUseCase = removeLaborItemFromWorkOrderUseCase;
    }


    @Override
    @PostMapping("/{laborId}/add")
    public ResponseEntity<Void> addLabor(@PathVariable UUID workOrderId, @PathVariable UUID laborId) {
        addLaborToWorkOrderUseCase.handle(new AddLaborToWorkOrderCommand(workOrderId, laborId));
        return ResponseEntity.noContent().build();
    }

    @Override
    @PostMapping("/{laborItemId}/remove")
    public ResponseEntity<Void> removeLabor(@PathVariable UUID workOrderId, @PathVariable UUID laborItemId) {
        removeLaborItemFromWorkOrderUseCase.handle(new RemoveLaborItemFromWorkOrderCommand(workOrderId, laborItemId));
        return ResponseEntity.noContent().build();
    }

    @Override
    @PostMapping("/{laborItemId}/start")
    public ResponseEntity<Void> startLabor(@PathVariable UUID workOrderId, @PathVariable UUID laborItemId) {
        startLaborExecutionUseCase.handle(new StartLaborExecutionCommand(workOrderId, laborItemId));
        return ResponseEntity.noContent().build();
    }

    @Override
    @PostMapping("/{laborItemId}/finish")
    public ResponseEntity<Void> finishLabor(@PathVariable UUID workOrderId, @PathVariable UUID laborItemId) {
        finishLaborExecutionUseCase.handle(new FinishLaborExecutionCommand(workOrderId, laborItemId));
        return ResponseEntity.noContent().build();
    }

    @Override
    @GetMapping("/{laborItemId}")
    public ResponseEntity<WorkOrderLaborItemResponse> findById(@PathVariable UUID workOrderId, @PathVariable UUID laborItemId) {
        WorkOrderLaborItem laborItem = getWorkOrderLaborItemByIdUseCase.handle(new GetWorkOrderLaborItemByIdQuery(workOrderId, laborItemId));
        return ResponseEntity.ok(new WorkOrderLaborItemResponse(laborItem));
    }
}
