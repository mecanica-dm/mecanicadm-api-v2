package com.mecanicadm.mecanicadm_api.infra.features.client.api.dto.request;

import com.mecanicadm.mecanicadm_api.shared.validation.annotation.CpfCnpj;
import com.mecanicadm.mecanicadm_api.shared.validation.annotation.Phone;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateClientRequest(
        @NotBlank(message = "{validation.client.name.not.blank}")
        @Size(max = 255, message = "{validation.field.size.max}")
        String name,

        @NotBlank(message = "{validation.client.email.not.blank}")
        @Email(message = "{validation.client.email.invalid}")
        @Size(max = 255, message = "{validation.field.size.max}")
        String email,

        @NotBlank(message = "{validation.client.document.not.blank}")
        @CpfCnpj
        @Size(max = 20, message = "{validation.field.size.max}")
        String document,

        @Phone
        @Size(max = 20, message = "{validation.field.size.max}")
        String phone
) {
}
