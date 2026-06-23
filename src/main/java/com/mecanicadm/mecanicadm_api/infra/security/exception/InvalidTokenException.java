package com.mecanicadm.mecanicadm_api.infra.security.exception;

import com.mecanicadm.mecanicadm_api.infra.security.exception.SecurityException;

public class InvalidTokenException extends SecurityException {
    public InvalidTokenException() {
        super("token.invalid.expired");
    }
}
