package com.mecanicadm.mecanicadm_api.infra.features.client.api.dto.request;

import com.mecanicadm.mecanicadm_api.shared.validation.annotation.CpfCnpj;
import com.mecanicadm.mecanicadm_api.shared.validation.annotation.Phone;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record CreateClientRequest(
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
