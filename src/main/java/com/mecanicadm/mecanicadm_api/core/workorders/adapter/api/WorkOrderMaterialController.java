package com.mecanicadm.mecanicadm_api.core.workorders.adapter.api;

import com.mecanicadm.mecanicadm_api.core.workorders.adapter.api.openapi.WorkOrderMaterialOpenApi;
import com.mecanicadm.mecanicadm_api.core.workorders.usecase.AddMaterialToWorkOrderUseCase;
import com.mecanicadm.mecanicadm_api.core.workorders.usecase.RemoveMaterialItemFromWorkOrderUseCase;
import com.mecanicadm.mecanicadm_api.core.workorders.usecase.command.AddMaterialToWorkOrderCommand;
import com.mecanicadm.mecanicadm_api.core.workorders.usecase.command.RemoveMaterialItemFromWorkOrderCommand;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/work-orders/{workOrderId}/materials")
public class WorkOrderMaterialController implements WorkOrderMaterialOpenApi {

    private final AddMaterialToWorkOrderUseCase addMaterialToWorkOrderUseCase;
    private final RemoveMaterialItemFromWorkOrderUseCase removeMaterialItemFromWorkOrderUseCase;

    public WorkOrderMaterialController(AddMaterialToWorkOrderUseCase addMaterialToWorkOrderUseCase,
                                       RemoveMaterialItemFromWorkOrderUseCase removeMaterialItemFromWorkOrderUseCase) {
        this.addMaterialToWorkOrderUseCase = addMaterialToWorkOrderUseCase;
        this.removeMaterialItemFromWorkOrderUseCase = removeMaterialItemFromWorkOrderUseCase;
    }

    @Override
    @PostMapping("/{materialId}/add")
    public ResponseEntity<Void> addMaterial(@PathVariable UUID workOrderId, @PathVariable UUID materialId, @Valid @RequestBody AddMaterialToWorkOrderCommand cmd) {
        addMaterialToWorkOrderUseCase.handle(cmd.withWorkOrderIdAndMaterialId(workOrderId, materialId));
        return ResponseEntity.noContent().build();
    }

    @Override
    @PostMapping("/{materialId}/remove")
    public ResponseEntity<Void> removeMaterial(@PathVariable UUID workOrderId, @PathVariable UUID materialId) {
        removeMaterialItemFromWorkOrderUseCase.handle(new RemoveMaterialItemFromWorkOrderCommand(workOrderId, materialId));
        return ResponseEntity.noContent().build();
    }
}