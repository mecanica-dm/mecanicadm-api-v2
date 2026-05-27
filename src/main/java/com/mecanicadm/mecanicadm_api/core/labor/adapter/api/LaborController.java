package com.mecanicadm.mecanicadm_api.core.labor.adapter.api;

import com.mecanicadm.mecanicadm_api.core.labor.adapter.api.dto.LaborResponse;
import com.mecanicadm.mecanicadm_api.core.labor.adapter.api.openapi.LaborOpenApi;
import com.mecanicadm.mecanicadm_api.core.labor.usecase.CreateLaborUseCase;
import com.mecanicadm.mecanicadm_api.core.labor.usecase.DeleteLaborUseCase;
import com.mecanicadm.mecanicadm_api.core.labor.usecase.GetAllLaborsUseCase;
import com.mecanicadm.mecanicadm_api.core.labor.usecase.GetLaborByIdUseCase;
import com.mecanicadm.mecanicadm_api.core.labor.usecase.UpdateLaborUseCase;
import com.mecanicadm.mecanicadm_api.core.labor.usecase.command.CreateLaborCommand;
import com.mecanicadm.mecanicadm_api.core.labor.usecase.command.DeleteLaborCommand;
import com.mecanicadm.mecanicadm_api.core.labor.usecase.command.UpdateLaborCommand;
import com.mecanicadm.mecanicadm_api.core.labor.usecase.query.GetLaborByIdQuery;
import com.mecanicadm.mecanicadm_api.core.labor.usecase.query.SearchLaborsQuery;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
@RequestMapping("/labor")
public class LaborController implements LaborOpenApi {

    private final CreateLaborUseCase createLaborUseCase;
    private final UpdateLaborUseCase updateLaborUseCase;
    private final DeleteLaborUseCase deleteLaborUseCase;
    private final GetLaborByIdUseCase getLaborByIdUseCase;
    private final GetAllLaborsUseCase getAllLaborsUseCase;

    @Autowired
    public LaborController(CreateLaborUseCase createLaborUseCase, UpdateLaborUseCase updateLaborUseCase, DeleteLaborUseCase deleteLaborUseCase, GetLaborByIdUseCase getLaborByIdUseCase, GetAllLaborsUseCase getAllLaborsUseCase) {
        this.createLaborUseCase = createLaborUseCase;
        this.updateLaborUseCase = updateLaborUseCase;
        this.deleteLaborUseCase = deleteLaborUseCase;
        this.getLaborByIdUseCase = getLaborByIdUseCase;
        this.getAllLaborsUseCase = getAllLaborsUseCase;
    }

    @Override
    @PostMapping
    public ResponseEntity<UUID> create(@Valid @RequestBody CreateLaborCommand cmd) {
        UUID id = createLaborUseCase.handle(cmd);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(id).toUri();
        return ResponseEntity.created(uri).body(id);
    }

    @Override
    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable UUID id, @Valid @RequestBody UpdateLaborCommand cmd) {
        updateLaborUseCase.handle(cmd.withId(id));
        return ResponseEntity.noContent().build();
    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        deleteLaborUseCase.handle(new DeleteLaborCommand(id));
        return ResponseEntity.noContent().build();
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<LaborResponse> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(getLaborByIdUseCase.handle(new GetLaborByIdQuery(id)));
    }

    @Override
    @GetMapping
    public ResponseEntity<Page<LaborResponse>> getAllLabor(@RequestParam(required = false) String name,
                                                           @PageableDefault(size = 20) Pageable pageable) {
        SearchLaborsQuery query = new SearchLaborsQuery(name, pageable);
        Page<LaborResponse> pageResult = getAllLaborsUseCase.handle(query);
        return ResponseEntity.ok(pageResult);
    }

}
