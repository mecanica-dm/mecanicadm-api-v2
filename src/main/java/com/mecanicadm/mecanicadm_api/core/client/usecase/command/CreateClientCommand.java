package com.mecanicadm.mecanicadm_api.core.client.usecase.command;

import com.mecanicadm.mecanicadm_api.infra.validation.annotation.CpfCnpj;
import com.mecanicadm.mecanicadm_api.infra.validation.annotation.Phone;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record CreateClientCommand(
        @NotBlank(message = "{validation.client.name.not.blank}")
        String name,

        @NotBlank(message = "{validation.client.email.not.blank}")
        @Email(message = "{validation.client.email.invalid}")
        String email,

        @NotBlank(message = "{validation.client.document.not.blank}")
        @CpfCnpj
        String document,

        @NotBlank(message = "{validation.client.phone.not.blank}")
        @Phone
        String phone
) {
}
