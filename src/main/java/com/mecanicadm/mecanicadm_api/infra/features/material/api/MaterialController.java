package com.mecanicadm.mecanicadm_api.infra.features.material.api;

import com.mecanicadm.mecanicadm_api.core.material.domain.enums.MaterialType;
import com.mecanicadm.mecanicadm_api.core.material.usecase.CreateMaterialUseCase;
import com.mecanicadm.mecanicadm_api.core.material.usecase.GetAllMaterialsUseCase;
import com.mecanicadm.mecanicadm_api.core.material.usecase.GetMaterialByIdUseCase;
import com.mecanicadm.mecanicadm_api.core.material.usecase.SoftDeleteMaterialUseCase;
import com.mecanicadm.mecanicadm_api.core.material.usecase.UpdateMaterialUseCase;
import com.mecanicadm.mecanicadm_api.core.material.usecase.command.CreateMaterialCommand;
import com.mecanicadm.mecanicadm_api.core.material.usecase.command.SoftDeleteMaterialCommand;
import com.mecanicadm.mecanicadm_api.core.material.usecase.command.UpdateMaterialCommand;
import com.mecanicadm.mecanicadm_api.core.material.usecase.query.GetMaterialByIdQuery;
import com.mecanicadm.mecanicadm_api.core.material.usecase.query.SearchMaterialsQuery;
import com.mecanicadm.mecanicadm_api.infra.features.material.api.dto.request.CreateMaterialRequest;
import com.mecanicadm.mecanicadm_api.infra.features.material.api.dto.request.UpdateMaterialRequest;
import com.mecanicadm.mecanicadm_api.infra.features.material.api.dto.response.MaterialResponse;
import com.mecanicadm.mecanicadm_api.infra.features.material.api.openapi.MaterialOpenApi;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
@RequestMapping("/materials")
public class MaterialController implements MaterialOpenApi {

    private final CreateMaterialUseCase createMaterialUseCase;
    private final UpdateMaterialUseCase updateMaterialUseCase;
    private final GetMaterialByIdUseCase getMaterialByIdUseCase;
    private final GetAllMaterialsUseCase getAllMaterialsUseCase;
    private final SoftDeleteMaterialUseCase softDeleteMaterialUseCase;

    public MaterialController(CreateMaterialUseCase createMaterialUseCase,
                              UpdateMaterialUseCase updateMaterialUseCase,
                              GetMaterialByIdUseCase getMaterialByIdUseCase,
                              GetAllMaterialsUseCase getAllMaterialsUseCase,
                              SoftDeleteMaterialUseCase softDeleteMaterialUseCase) {
        this.createMaterialUseCase = createMaterialUseCase;
        this.updateMaterialUseCase = updateMaterialUseCase;
        this.getMaterialByIdUseCase = getMaterialByIdUseCase;
        this.getAllMaterialsUseCase = getAllMaterialsUseCase;
        this.softDeleteMaterialUseCase = softDeleteMaterialUseCase;
    }

    @Override
    @PostMapping
    public ResponseEntity<UUID> create(@Valid @RequestBody CreateMaterialRequest request) {
        UUID materialId = createMaterialUseCase.execute(new CreateMaterialCommand(request.name(), request.brand(),
                request.description(), request.price(), request.type(), request.quantity()));
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(materialId).toUri();
        return ResponseEntity.created(uri).body(materialId);
    }

    @Override
    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable UUID id, @Valid @RequestBody UpdateMaterialRequest request) {
        updateMaterialUseCase.execute(new UpdateMaterialCommand(id, request.name(), request.brand(),
                request.description(), request.price(), request.type()));
        return ResponseEntity.ok().build();
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<MaterialResponse> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(new MaterialResponse(getMaterialByIdUseCase.execute(new GetMaterialByIdQuery(id))));
    }

    @Override
    @GetMapping
    @Operation(summary = "Listar com Filtros", description = "Lista materiais com paginação e filtros opcionais.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Page.class)))
    })
    public ResponseEntity<Page<MaterialResponse>> findAll(
            @Parameter(description = "Nome do material") @RequestParam(required = false) String name,
            @Parameter(description = "Marca do material") @RequestParam(required = false) String brand,
            @Parameter(description = "Tipo do material") @RequestParam(required = false) MaterialType type,
            @PageableDefault(size = 20) Pageable pageable
    ) {
        var sort = pageable.getSort().get().findFirst();
        var sortBy = sort.map(Sort.Order::getProperty).orElse("name");
        var direction = sort.map(s -> s.getDirection().name()).orElse("ASC");

        var query = new SearchMaterialsQuery(name, brand, type, pageable.getPageNumber(), pageable.getPageSize(), sortBy, direction);
        var result = getAllMaterialsUseCase.execute(query);

        var responseList = result.items().stream().map(MaterialResponse::new).toList();
        var response = new PageImpl<>(responseList, pageable, result.totalElements());
        return ResponseEntity.ok(response);
    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        softDeleteMaterialUseCase.execute(new SoftDeleteMaterialCommand(id));
        return ResponseEntity.noContent().build();
    }
}
