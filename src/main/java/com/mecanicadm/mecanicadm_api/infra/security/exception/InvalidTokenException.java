package com.mecanicadm.mecanicadm_api.infra.security.exception;

import com.mecanicadm.mecanicadm_api.infra.exception.SecurityException;

public class InvalidTokenException extends SecurityException {
    public InvalidTokenException() {
        super("token.invalid.expired");
    }
}
