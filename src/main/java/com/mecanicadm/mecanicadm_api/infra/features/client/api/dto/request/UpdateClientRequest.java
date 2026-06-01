package com.mecanicadm.mecanicadm_api.infra.features.client.api.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mecanicadm.mecanicadm_api.core.client.usecase.command.UpdateClientCommand;
import com.mecanicadm.mecanicadm_api.infra.validation.annotation.CpfCnpj;
import com.mecanicadm.mecanicadm_api.infra.validation.annotation.Phone;
import jakarta.validation.constraints.Email;

import java.util.UUID;

public record UpdateClientRequest(
        @JsonIgnore
        UUID id,

        String name,

        @Email(message = "{validation.client.email.invalid}")
        String email,

        @CpfCnpj
        String document,

        @Phone
        String phone
) {
    public UpdateClientCommand withId(UUID id) {
        return new UpdateClientCommand(id, this.name, this.email, this.document, this.phone);
    }
}
