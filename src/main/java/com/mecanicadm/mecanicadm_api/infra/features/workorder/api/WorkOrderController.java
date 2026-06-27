package com.mecanicadm.mecanicadm_api.infra.features.workorder.api;

import com.mecanicadm.mecanicadm_api.core.workorders.domain.WorkOrder;
import com.mecanicadm.mecanicadm_api.core.workorders.usecase.CreateWorkOrderUseCase;
import com.mecanicadm.mecanicadm_api.core.workorders.usecase.GetAllWorkOrderUseCase;
import com.mecanicadm.mecanicadm_api.core.workorders.usecase.GetWorkOrderByIdUseCase;
import com.mecanicadm.mecanicadm_api.core.workorders.usecase.SoftDeleteWorkOrderUseCase;
import com.mecanicadm.mecanicadm_api.core.workorders.usecase.UpdateWorkOrderUseCase;
import com.mecanicadm.mecanicadm_api.core.workorders.usecase.command.CreateWorkOrderCommand;
import com.mecanicadm.mecanicadm_api.core.workorders.usecase.command.SoftDeleteWorkOrderCommand;
import com.mecanicadm.mecanicadm_api.core.workorders.usecase.command.UpdateWorkOrderCommand;
import com.mecanicadm.mecanicadm_api.core.workorders.usecase.query.GetAllWorkOrdersQuery;
import com.mecanicadm.mecanicadm_api.core.workorders.usecase.query.GetWorkOrderByIdQuery;
import com.mecanicadm.mecanicadm_api.infra.features.workorder.api.dto.request.CreateWorkOrderRequest;
import com.mecanicadm.mecanicadm_api.infra.features.workorder.api.dto.request.UpdateWorkOrderRequest;
import com.mecanicadm.mecanicadm_api.infra.features.workorder.api.dto.response.WorkOrderResponse;
import com.mecanicadm.mecanicadm_api.infra.features.workorder.api.openapi.WorkOrderOpenApi;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/work-orders")
public class WorkOrderController implements WorkOrderOpenApi {

    private final CreateWorkOrderUseCase createWorkOrderUseCase;
    private final UpdateWorkOrderUseCase updateWorkOrderUseCase;
    private final GetWorkOrderByIdUseCase getWorkOrderByIdUseCase;
    private final GetAllWorkOrderUseCase getAllWorkOrderUseCase;
    private final SoftDeleteWorkOrderUseCase softDeleteWorkOrderUseCase;

    public WorkOrderController(CreateWorkOrderUseCase createWorkOrderUseCase,
                               UpdateWorkOrderUseCase updateWorkOrderUseCase,
                               GetWorkOrderByIdUseCase getWorkOrderByIdUseCase,
                               GetAllWorkOrderUseCase getAllWorkOrderUseCase,
                               SoftDeleteWorkOrderUseCase softDeleteWorkOrderUseCase) {
        this.createWorkOrderUseCase = createWorkOrderUseCase;
        this.updateWorkOrderUseCase = updateWorkOrderUseCase;
        this.getWorkOrderByIdUseCase = getWorkOrderByIdUseCase;
        this.getAllWorkOrderUseCase = getAllWorkOrderUseCase;
        this.softDeleteWorkOrderUseCase = softDeleteWorkOrderUseCase;
    }

    @Override
    @PostMapping
    public ResponseEntity<UUID> create(@Valid @RequestBody CreateWorkOrderRequest request) {
        UUID workOrderId = createWorkOrderUseCase.execute(
                new CreateWorkOrderCommand(request.clientId(), request.vehicleId(), request.description()));
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(workOrderId).toUri();
        return ResponseEntity.created(uri).body(workOrderId);
    }

    @Override
    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable UUID id, @Valid @RequestBody UpdateWorkOrderRequest request) {
        updateWorkOrderUseCase.execute(
                new UpdateWorkOrderCommand(id, request.vehicleId(), request.clientId(), request.description()));
        return ResponseEntity.ok().build();
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<WorkOrderResponse> findById(@PathVariable UUID id) {
        WorkOrder workOrder = getWorkOrderByIdUseCase.execute(new GetWorkOrderByIdQuery(id));
        return ResponseEntity.ok(new WorkOrderResponse(workOrder));
    }

    @Override
    @GetMapping
    public ResponseEntity<Page<WorkOrderResponse>> getAll(
            @RequestParam(required = false) UUID clientId,
            @RequestParam(required = false) String licensePlate,
            @PageableDefault(size = 20) Pageable pageable) {

        var sort = pageable.getSort().get().findFirst();
        var sortBy = sort.map(Sort.Order::getProperty).orElse("executionStartAt");
        var direction = sort.map(s -> s.getDirection().name()).orElse("DESC");

        var query = new GetAllWorkOrdersQuery(clientId, licensePlate, pageable.getPageNumber(), pageable.getPageSize(), sortBy, direction);
        var result = getAllWorkOrderUseCase.execute(query);

        var responseList = result.items().stream()
                .map(WorkOrderResponse::new)
                .toList();

        Page<WorkOrderResponse> response = new PageImpl<>(responseList, pageable, result.totalElements());
        return ResponseEntity.ok(response);
    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        softDeleteWorkOrderUseCase.execute(new SoftDeleteWorkOrderCommand(id));
        return ResponseEntity.noContent().build();
    }
}