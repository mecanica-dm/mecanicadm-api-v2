package com.mecanicadm.mecanicadm_api.infra.features.labor.api.openapi;

import com.mecanicadm.mecanicadm_api.infra.features.labor.api.dto.request.CreateLaborRequest;
import com.mecanicadm.mecanicadm_api.infra.features.labor.api.dto.request.UpdateLaborRequest;
import com.mecanicadm.mecanicadm_api.infra.features.labor.api.dto.response.LaborResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Tag(name = "Mao de obra", description = "Endpoints para gerenciamento de mao de obra")
public abstract class LaborOpenApi {

    @PostMapping
    @Operation(summary = "Criar novo servico")
    @ApiResponse(responseCode = "201", description = "Servico cadastrado com sucesso")
    public abstract ResponseEntity<UUID> create(@Valid @RequestBody CreateLaborRequest request);

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar servico")
    @ApiResponse(responseCode = "204", description = "Servico atualizado com sucesso")
    @ApiResponse(responseCode = "404", description = "Servico nao encontrado")
    public abstract ResponseEntity<Void> update(@Parameter(description = "Identificador do servico") @PathVariable UUID id,
                                                @Valid @RequestBody UpdateLaborRequest request);

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir servico")
    @ApiResponse(responseCode = "204", description = "Servico excluido com sucesso")
    @ApiResponse(responseCode = "404", description = "Servico nao encontrado")
    public abstract ResponseEntity<Void> delete(@Parameter(description = "Identificador do servico") @PathVariable UUID id);

    @GetMapping("/{id}")
    @Operation(summary = "Buscar servico por id")
    @ApiResponse(responseCode = "200", description = "Servico encontrado")
    @ApiResponse(responseCode = "404", description = "Servico nao encontrado")
    public abstract ResponseEntity<LaborResponse> findById(@Parameter(description = "Identificador do servico") @PathVariable UUID id);

    @GetMapping
    @Operation(summary = "Listar servicos")
    @ApiResponse(responseCode = "200", description = "Lista recuperada com sucesso")
    public abstract ResponseEntity<Page<LaborResponse>> getAll(
            @Parameter(description = "Filtro parcial por nome") @RequestParam(required = false) String name,
            @ParameterObject @PageableDefault(size = 20) Pageable pageable);
}

