package com.mecanicadm.mecanicadm_api.core.client.adapter.api;

import com.mecanicadm.mecanicadm_api.core.client.adapter.api.dto.ClientResponse;
import com.mecanicadm.mecanicadm_api.core.client.adapter.api.openapi.ClientOpenApi;
import com.mecanicadm.mecanicadm_api.core.client.usecase.CreateClientUseCase;
import com.mecanicadm.mecanicadm_api.core.client.usecase.GetAllClientUseCase;
import com.mecanicadm.mecanicadm_api.core.client.usecase.SoftDeleteClientUseCase;
import com.mecanicadm.mecanicadm_api.core.client.usecase.UpdateClientUseCase;
import com.mecanicadm.mecanicadm_api.core.client.usecase.command.CreateClientCommand;
import com.mecanicadm.mecanicadm_api.core.client.usecase.command.SoftDeleteClientCommand;
import com.mecanicadm.mecanicadm_api.core.client.usecase.command.UpdateClientCommand;
import com.mecanicadm.mecanicadm_api.core.client.usecase.query.GetAllClientQuery;
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
@RequestMapping("/clients")
public class ClientController implements ClientOpenApi {

    private final CreateClientUseCase createClientUseCase;
    private final GetAllClientUseCase getAllClientUseCase;
    private final UpdateClientUseCase updateClientUseCase;
    private final SoftDeleteClientUseCase softDeleteClientUseCase;

    @Autowired
    public ClientController(CreateClientUseCase createClientUseCase,
                            GetAllClientUseCase getAllClientUseCase,
                            UpdateClientUseCase updateClientUseCase,
                            SoftDeleteClientUseCase softDeleteClientUseCase) {
        this.createClientUseCase = createClientUseCase;
        this.getAllClientUseCase = getAllClientUseCase;
        this.updateClientUseCase = updateClientUseCase;
        this.softDeleteClientUseCase = softDeleteClientUseCase;
    }

    @Override
    @PostMapping
    public ResponseEntity<UUID> create(@RequestBody @Valid CreateClientCommand cmd) {
        UUID clientId = createClientUseCase.handle(cmd);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(clientId).toUri();
        return ResponseEntity.created(uri).body(clientId);
    }

    @Override
    @GetMapping
    public ResponseEntity<Page<ClientResponse>> list(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String document,
            @PageableDefault(size = 20) Pageable pageable
    ) {
        Page<ClientResponse> clients = getAllClientUseCase.handle(new GetAllClientQuery(name, document, pageable));
        return ResponseEntity.ok(clients);
    }

    @Override
    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable UUID id, @RequestBody @Valid UpdateClientCommand cmd) {
        updateClientUseCase.handle(cmd.withId(id));
        return ResponseEntity.ok().build();
    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        softDeleteClientUseCase.handle(new SoftDeleteClientCommand(id));
        return ResponseEntity.noContent().build();
    }
}
