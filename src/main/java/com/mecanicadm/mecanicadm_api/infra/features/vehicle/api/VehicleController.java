package com.mecanicadm.mecanicadm_api.infra.features.vehicle.api;

import com.mecanicadm.mecanicadm_api.core.vehicle.domain.Vehicle;
import com.mecanicadm.mecanicadm_api.core.vehicle.usecase.*;
import com.mecanicadm.mecanicadm_api.core.vehicle.usecase.command.CreateVehicleCommand;
import com.mecanicadm.mecanicadm_api.core.vehicle.usecase.command.DeleteVehicleCommand;
import com.mecanicadm.mecanicadm_api.core.vehicle.usecase.command.UpdateVehicleCommand;
import com.mecanicadm.mecanicadm_api.core.vehicle.usecase.query.GetAllVehiclesQuery;
import com.mecanicadm.mecanicadm_api.core.vehicle.usecase.query.GetVehicleByIdQuery;
import com.mecanicadm.mecanicadm_api.infra.features.vehicle.api.dto.openapi.VehicleOpenApi;
import com.mecanicadm.mecanicadm_api.infra.features.vehicle.api.dto.request.CreateVehicleRequest;
import com.mecanicadm.mecanicadm_api.infra.features.vehicle.api.dto.request.UpdateVehicleRequest;
import com.mecanicadm.mecanicadm_api.infra.features.vehicle.api.dto.response.VehicleResponse;
import com.mecanicadm.mecanicadm_api.infra.validation.annotation.LicensePlate;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/vehicle")
@Validated
public class VehicleController extends VehicleOpenApi {

    private final CreateVehicleUseCase createVehicleUseCase;
    private final UpdateVehicleUseCase updateVehicleUseCase;
    private final DeleteVehicleUseCase deleteVehicleUseCase;
    private final GetVehicleByIdUseCase getVehicleByIdUseCase;
    private final GetAllVehicleUseCase getAllVehicleUseCase;

    public VehicleController(CreateVehicleUseCase createVehicleUseCase,
                             UpdateVehicleUseCase updateVehicleUseCase,
                             DeleteVehicleUseCase deleteVehicleUseCase,
                             GetVehicleByIdUseCase getVehicleByIdUseCase,
                             GetAllVehicleUseCase getAllVehicleUseCase) {
        this.createVehicleUseCase = createVehicleUseCase;
        this.updateVehicleUseCase = updateVehicleUseCase;
        this.deleteVehicleUseCase = deleteVehicleUseCase;
        this.getVehicleByIdUseCase = getVehicleByIdUseCase;
        this.getAllVehicleUseCase = getAllVehicleUseCase;
    }

    @PostMapping
    public ResponseEntity<String> create(@Valid @RequestBody CreateVehicleRequest request) {
        var command = new CreateVehicleCommand(request.model(), request.licensePlate(), request.brand(), request.modelYear());
        String licensePlate = createVehicleUseCase.execute(command);
        URI location = URI.create(String.format("/vehicle/%s", licensePlate));
        return ResponseEntity.created(location).body(licensePlate);
    }

    @PutMapping("/{licensePlate}")
    public ResponseEntity<Void> update(@PathVariable @LicensePlate String licensePlate, @Valid @RequestBody UpdateVehicleRequest request) {
        var command = new UpdateVehicleCommand(licensePlate, request.model(), request.brand(), request.modelYear());
        updateVehicleUseCase.execute(command);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{licensePlate}")
    public ResponseEntity<Void> delete(@PathVariable @LicensePlate String licensePlate) {
        deleteVehicleUseCase.execute(new DeleteVehicleCommand(licensePlate));
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{licensePlate}")
    public ResponseEntity<VehicleResponse> findById(@PathVariable @LicensePlate String licensePlate) {
        Vehicle vehicle = getVehicleByIdUseCase.execute(new GetVehicleByIdQuery(licensePlate));
        VehicleResponse response = new VehicleResponse(vehicle.getModel(), vehicle.getLicensePlate(), vehicle.getBrand(), vehicle.getModelYear());
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<Page<VehicleResponse>> getAll(
            @RequestParam(required = false) String licensePlate,
            @RequestParam(required = false) String model,
            @RequestParam(required = false) String brand,
            @RequestParam(required = false) Short modelYear,
            @PageableDefault(size = 20) Pageable pageable) {

        var sort = pageable.getSort().get().findFirst();
        var sortBy = sort.map(Sort.Order::getProperty).orElse("licensePlate");
        var direction = sort.map(s -> s.getDirection().name()).orElse("ASC");

        var query = new GetAllVehiclesQuery(licensePlate, model, brand, modelYear, pageable.getPageNumber(), pageable.getPageSize(), sortBy, direction);
        var result = getAllVehicleUseCase.execute(query);

        var responseList = result.items().stream()
                .map(v -> new VehicleResponse(v.getModel(), v.getLicensePlate(), v.getBrand(), v.getModelYear()))
                .toList();

        Page<VehicleResponse> response = new PageImpl<>(responseList, pageable, result.totalElements());

        return ResponseEntity.ok(response);
    }
}
