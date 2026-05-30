package com.mecanicadm.mecanicadm_api.infra.features.vehicle.api.dto.openapi;

import com.mecanicadm.mecanicadm_api.infra.features.vehicle.api.dto.request.CreateVehicleRequest;
import com.mecanicadm.mecanicadm_api.infra.features.vehicle.api.dto.request.UpdateVehicleRequest;
import com.mecanicadm.mecanicadm_api.infra.features.vehicle.api.dto.response.VehicleResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

@Tag(name = "Veículos", description = "Endpoints para gerenciamento de veículos")
public abstract class VehicleOpenApi {
    @Operation(summary = "Criar novo veículo", description = "Cadastra um novo veículo no sistema")
    @ApiResponse(responseCode = "201", description = "Veículo cadastrado com sucesso")
    public abstract ResponseEntity<String> create(CreateVehicleRequest cmd);

    @Operation(summary = "Atualizar veículo", description = "Atualiza os dados de um veículo existente através da placa")
    @ApiResponse(responseCode = "204", description = "Veículo atualizado com sucesso")
    @ApiResponse(responseCode = "404", description = "Veículo não encontrado")
    public abstract ResponseEntity<Void> update(@Parameter(description = "Identificador do veículo a ser atualizado") String licensePlate, UpdateVehicleRequest cmd);

    @Operation(summary = "Excluir veículo", description = "Exclui um veículo existente através da placa")
    @ApiResponse(responseCode = "204", description = "Veículo excluído com sucesso")
    @ApiResponse(responseCode = "404", description = "Veículo não encontrado")
    public abstract ResponseEntity<Void> delete(@Parameter(description = "Identificador do veículo a ser excluído") String licensePlate);

    @Operation(summary = "Buscar veículo", description = "Busca os detalhes de um veículo através da placa")
    @ApiResponse(responseCode = "200", description = "Veículo encontrado com sucesso")
    @ApiResponse(responseCode = "404", description = "Veículo não encontrado")
    public abstract ResponseEntity<VehicleResponse> findById(@Parameter(description = "Placa do veículo a ser buscado") String licensePlate);

    @Operation(summary = "Listar veículos", description = "Lista veículos com paginação e filtros opcionais")
    @ApiResponse(responseCode = "200", description = "Lista recuperada com sucesso")
    public abstract ResponseEntity<Page<VehicleResponse>> getAll(
            @Parameter(description = "Filtro parcial por placa") String licensePlate,
            @Parameter(description = "Filtro parcial por modelo") String model,
            @Parameter(description = "Filtro equal por marca") String brand,
            @Parameter(description = "Filtro equal por ano") Short modelYear,
            @ParameterObject Pageable pageable);
}
