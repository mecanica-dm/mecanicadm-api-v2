package com.mecanicadm.mecanicadm_api.core.workorders.adapter.api;

import com.mecanicadm.mecanicadm_api.core.workorders.adapter.api.openapi.WorkOrderBudgetOpenApi;
import com.mecanicadm.mecanicadm_api.core.workorders.usecase.CalculateWorkOrderBudgetUseCase;
import com.mecanicadm.mecanicadm_api.core.workorders.usecase.DecideWorkOrderBudgetUseCase;
import com.mecanicadm.mecanicadm_api.core.workorders.usecase.ManuallyAdjustWorkOrderBudgetUseCase;
import com.mecanicadm.mecanicadm_api.core.workorders.usecase.SendWorkOrderBudgetUseCase;
import com.mecanicadm.mecanicadm_api.core.workorders.usecase.GetPrintableBudgetUseCase;
import com.mecanicadm.mecanicadm_api.core.workorders.usecase.command.CalculateWorkOrderBudgetCommand;
import com.mecanicadm.mecanicadm_api.core.workorders.usecase.command.DecideWorkOrderBudgetCommand;
import com.mecanicadm.mecanicadm_api.core.workorders.usecase.command.ManuallyAdjustWorkOrderBudgetCommand;
import com.mecanicadm.mecanicadm_api.core.workorders.usecase.command.SendWorkOrderBudgetCommand;
import com.mecanicadm.mecanicadm_api.core.workorders.usecase.dto.PrintableBudgetResponse;
import com.mecanicadm.mecanicadm_api.core.workorders.usecase.query.GetPrintableBudgetQuery;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/work-orders/{workOrderId}/budget")
public class WorkOrderBudgetController implements WorkOrderBudgetOpenApi {

    private final SendWorkOrderBudgetUseCase sendWorkOrderBudgetUseCase;
    private final ManuallyAdjustWorkOrderBudgetUseCase manuallyAdjustWorkOrderBudgetUseCase;
    private final DecideWorkOrderBudgetUseCase decideWorkOrderBudgetUseCase;
    private final CalculateWorkOrderBudgetUseCase calculateWorkOrderBudgetUseCase;
    private final GetPrintableBudgetUseCase getPrintableBudgetUseCase;

    public WorkOrderBudgetController(SendWorkOrderBudgetUseCase sendWorkOrderBudgetUseCase,
                                     ManuallyAdjustWorkOrderBudgetUseCase manuallyAdjustWorkOrderBudgetUseCase,
                                     DecideWorkOrderBudgetUseCase decideWorkOrderBudgetUseCase,
                                     CalculateWorkOrderBudgetUseCase calculateWorkOrderBudgetUseCase,
                                     GetPrintableBudgetUseCase getPrintableBudgetUseCase) {
        this.sendWorkOrderBudgetUseCase = sendWorkOrderBudgetUseCase;
        this.manuallyAdjustWorkOrderBudgetUseCase = manuallyAdjustWorkOrderBudgetUseCase;
        this.decideWorkOrderBudgetUseCase = decideWorkOrderBudgetUseCase;
        this.calculateWorkOrderBudgetUseCase = calculateWorkOrderBudgetUseCase;
        this.getPrintableBudgetUseCase = getPrintableBudgetUseCase;
    }

    @Override
    @PostMapping("/send")
    public ResponseEntity<Void> sendBudget(@PathVariable UUID workOrderId) {
        sendWorkOrderBudgetUseCase.handle(new SendWorkOrderBudgetCommand(workOrderId));
        return ResponseEntity.noContent().build();
    }

    @Override
    @PatchMapping
    public ResponseEntity<Void> adjustBudget(@PathVariable UUID workOrderId, @Valid @RequestBody ManuallyAdjustWorkOrderBudgetCommand cmd) {
        manuallyAdjustWorkOrderBudgetUseCase.handle(cmd.withId(workOrderId));
        return ResponseEntity.noContent().build();
    }

    @Override
    @PostMapping("/recalculate")
    public ResponseEntity<Void> recalculateBudget(@PathVariable UUID workOrderId) {
        calculateWorkOrderBudgetUseCase.handle(new CalculateWorkOrderBudgetCommand(workOrderId));
        return ResponseEntity.noContent().build();
    }

    @Override
    @PostMapping("/decision")
    public ResponseEntity<Void> decideBudget(@PathVariable UUID workOrderId, @Valid @RequestBody DecideWorkOrderBudgetCommand cmd) {
        decideWorkOrderBudgetUseCase.handle(cmd.withId(workOrderId));
        return ResponseEntity.noContent().build();
    }

    @Override
    @GetMapping("/print")
    public ResponseEntity<PrintableBudgetResponse> printBudget(@PathVariable UUID workOrderId) {
        PrintableBudgetResponse response = getPrintableBudgetUseCase.handle(new GetPrintableBudgetQuery(workOrderId));
        return ResponseEntity.ok(response);
    }
}
