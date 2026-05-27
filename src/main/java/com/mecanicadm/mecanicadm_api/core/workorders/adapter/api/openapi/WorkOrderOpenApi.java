package com.mecanicadm.mecanicadm_api.core.workorders.adapter.api.openapi;

import com.mecanicadm.mecanicadm_api.core.workorders.adapter.api.dto.WorkOrderResponse;
import com.mecanicadm.mecanicadm_api.core.workorders.usecase.command.CreateWorkOrderCommand;
import com.mecanicadm.mecanicadm_api.core.workorders.usecase.command.UpdateWorkOrderCommand;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

@Tag(name = "Ordens de Serviço", description = "Endpoints para gerenciamento de ordens de serviço")
public interface WorkOrderOpenApi {

    @Operation(summary = "Criar nova ordem de serviço", description = "Cadastra uma nova ordem de serviço no sistema")
    @ApiResponse(responseCode = "201", description = "Ordem de serviço criada com sucesso")
    @ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content)
    @ApiResponse(responseCode = "404", description = "Cliente, veículo, material ou serviço não encontrado", content = @Content)
    ResponseEntity<UUID> create(CreateWorkOrderCommand cmd);

    @Operation(summary = "Atualizar ordem de serviço", description = "Atualiza uma ordem de serviço existente no sistema")
    @ApiResponse(responseCode = "200", description = "Ordem de serviço atualizada com sucesso")
    @ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content)
    @ApiResponse(responseCode = "404", description = "Ordem de serviço não encontrada", content = @Content)
    ResponseEntity<Void> update(UUID id, UpdateWorkOrderCommand cmd);

    @Operation(summary = "Buscar ordem de serviço por ID", description = "Retorna os detalhes de uma ordem de serviço")
    @ApiResponse(responseCode = "200", description = "Ordem de serviço encontrada")
    @ApiResponse(responseCode = "404", description = "Ordem de serviço não encontrada", content = @Content)
    ResponseEntity<WorkOrderResponse> findById(UUID id);

    @Operation(summary = "Listar todas as ordens de serviço", description = "Retorna uma lista paginada de ordens de serviço")
    @ApiResponse(responseCode = "200", description = "Lista de ordens de serviço")
    ResponseEntity<Page<WorkOrderResponse>> getAll(
            UUID clientId,
            String licensePlate,
            @ParameterObject Pageable pageable);

    @Operation(summary = "Excluir ordem de serviço", description = "Remove uma ordem de serviço do sistema")
    @ApiResponse(responseCode = "204", description = "Ordem de serviço excluída com sucesso")
    @ApiResponse(responseCode = "404", description = "Ordem de serviço não encontrada", content = @Content)
    ResponseEntity<Void> delete(UUID id);
}