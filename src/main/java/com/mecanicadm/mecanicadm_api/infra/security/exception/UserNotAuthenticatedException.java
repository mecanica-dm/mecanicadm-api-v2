package com.mecanicadm.mecanicadm_api.infra.security.exception;

import com.mecanicadm.mecanicadm_api.infra.security.exception.SecurityException;

public class UserNotAuthenticatedException extends SecurityException {
    public UserNotAuthenticatedException() {
        super("error.user.not.authenticated");
    }
}
