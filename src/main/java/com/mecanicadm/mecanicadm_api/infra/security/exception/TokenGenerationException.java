package com.mecanicadm.mecanicadm_api.infra.security.exception;

import com.mecanicadm.mecanicadm_api.infra.exception.SecurityException;

public class TokenGenerationException extends SecurityException {
    public TokenGenerationException(String messageKey, Throwable cause) {
        super(messageKey, cause);
    }
}
