package com.mecanicadm.mecanicadm_api.core.labor.adapter.api.openapi;

import com.mecanicadm.mecanicadm_api.core.labor.adapter.api.dto.LaborResponse;
import com.mecanicadm.mecanicadm_api.core.labor.usecase.command.CreateLaborCommand;
import com.mecanicadm.mecanicadm_api.core.labor.usecase.command.UpdateLaborCommand;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

@Tag(name = "Mão de obra", description = "Endpoints para gerenciamento de mão de obra")
public interface LaborOpenApi {
    @Operation(summary = "Criar novo mão de obra", description = "Cadastra um novo mão de obra no sistema")
    @ApiResponse(responseCode = "201", description = "Mão de obra cadastrado com sucesso")
    ResponseEntity<UUID> create(CreateLaborCommand cmd);

    @Operation(summary = "Atualizar mão de obra", description = "Atualiza os dados de um mão de obra existente")
    @ApiResponse(responseCode = "204", description = "Mão de obra atualizado com sucesso")
    @ApiResponse(responseCode = "404", description = "Mão de obra não encontrado")
    ResponseEntity<Void> update(@Parameter(description = "Identificador do mão de obra a ser atualizado") UUID id, UpdateLaborCommand cmd);

    @Operation(summary = "Deletar mão de obra", description = "Remove um mão de obra existente do sistema")
    @ApiResponse(responseCode = "204", description = "Mão de obra deletado com sucesso")
    @ApiResponse(responseCode = "404", description = "Mão de obra não encontrado")
    ResponseEntity<Void> delete(@Parameter(description = "Identificador do mão de obra a ser deletado") UUID id);

    @Operation(summary = "Buscar mão de obra por ID", description = "Retorna os detalhes de um mão de obra específico")
    @ApiResponse(responseCode = "200", description = "Mão de obra encontrado", content = @Content(schema = @Schema(implementation = LaborResponse.class)))
    @ApiResponse(responseCode = "404", description = "Mão de obra não encontrado", content = @Content)
    ResponseEntity<LaborResponse> findById(@Parameter(description = "Identificador do mão de obra a ser buscado") UUID id);

    @Operation(summary = "Listar serviços", description = "Lista serviços com paginação e filtros opcionais")
    @ApiResponse(responseCode = "200", description = "Lista recuperada com sucesso")
    ResponseEntity<Page<LaborResponse>> getAllLabor(@Parameter(description = "Nome do mão de obra") String name,
                                                    @ParameterObject Pageable pageable);
}
