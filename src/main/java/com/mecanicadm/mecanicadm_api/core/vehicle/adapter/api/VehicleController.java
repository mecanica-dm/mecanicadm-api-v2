package com.mecanicadm.mecanicadm_api.core.vehicle.adapter.api;

import com.mecanicadm.mecanicadm_api.core.vehicle.adapter.api.dto.VehicleResponse;
import com.mecanicadm.mecanicadm_api.core.vehicle.adapter.api.openapi.VehicleOpenApi;
import com.mecanicadm.mecanicadm_api.core.vehicle.domain.Vehicle;
import com.mecanicadm.mecanicadm_api.core.vehicle.usecase.*;
import com.mecanicadm.mecanicadm_api.core.vehicle.usecase.command.CreateVehicleCommand;
import com.mecanicadm.mecanicadm_api.core.vehicle.usecase.command.DeleteVehicleCommand;
import com.mecanicadm.mecanicadm_api.core.vehicle.usecase.command.UpdateVehicleCommand;
import com.mecanicadm.mecanicadm_api.core.vehicle.usecase.query.GetAllVehiclesQuery;
import com.mecanicadm.mecanicadm_api.core.vehicle.usecase.query.GetVehicleByIdQuery;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/vehicle")
public class VehicleController implements VehicleOpenApi {

    private final CreateVehicleUseCase createVehicleUseCase;
    private final UpdateVehicleUseCase updateVehicleUseCase;
    private final DeleteVehicleUseCase deleteVehicleUseCase;
    private final GetVehicleByIdUseCase getVehicleByIdUseCase;
    private final GetAllVehicleUseCase getAllVehicleUseCase;

    @Autowired
    public VehicleController(CreateVehicleUseCase createVehicleUseCase, UpdateVehicleUseCase updateVehicleUseCase, DeleteVehicleUseCase deleteVehicleUseCase, GetVehicleByIdUseCase getVehicleByIdUseCase, GetAllVehicleUseCase getAllVehicleUseCase) {
        this.createVehicleUseCase = createVehicleUseCase;
        this.updateVehicleUseCase = updateVehicleUseCase;
        this.deleteVehicleUseCase = deleteVehicleUseCase;
        this.getVehicleByIdUseCase = getVehicleByIdUseCase;
        this.getAllVehicleUseCase = getAllVehicleUseCase;
    }

    @Override
    @PostMapping
    public ResponseEntity<String> create(@Valid @RequestBody CreateVehicleCommand cmd) {
        String licensePlate = createVehicleUseCase.handle(cmd);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(licensePlate).toUri();
        return ResponseEntity.created(uri).body(licensePlate);
    }

    @Override
    @PutMapping("/{licensePlate}")
    public ResponseEntity<Void> update(@PathVariable String licensePlate, @Valid @RequestBody UpdateVehicleCommand cmd) {
        updateVehicleUseCase.handle(cmd.withId(licensePlate));
        return ResponseEntity.noContent().build();
    }

    @Override
    @DeleteMapping("/{licensePlate}")
    public ResponseEntity<Void> delete(@PathVariable String licensePlate) {
        deleteVehicleUseCase.handle(new DeleteVehicleCommand(licensePlate));
        return ResponseEntity.noContent().build();
    }

    @Override
    @GetMapping("/{licensePlate}")
    public ResponseEntity<VehicleResponse> findVehicle(@PathVariable String licensePlate) {
        Vehicle vehicle = getVehicleByIdUseCase.handle(new GetVehicleByIdQuery(licensePlate));
        return ResponseEntity.ok(new VehicleResponse(vehicle));
    }

    @Override
    @GetMapping
    public ResponseEntity<Page<VehicleResponse>> getAllVehicle(
            @RequestParam(required = false) String licensePlate,
            @RequestParam(required = false) String model,
            @RequestParam(required = false) String brand,
            @RequestParam(required = false) Short modelYear,
            @PageableDefault(size = 20) Pageable pageable) {
        Page<Vehicle> vehicles = getAllVehicleUseCase.handle(new GetAllVehiclesQuery(licensePlate, model, brand, modelYear, pageable));
        return ResponseEntity.ok(vehicles.map(VehicleResponse::new));
    }
}
