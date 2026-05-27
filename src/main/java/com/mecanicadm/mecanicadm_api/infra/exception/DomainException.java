package com.mecanicadm.mecanicadm_api.infra.exception;

import org.springframework.http.HttpStatus;

public abstract class DomainException extends RuntimeException {
    private final transient Object[] args;
    private final HttpStatus status;

    protected DomainException(String messageKey, HttpStatus status, Object... args) {
        super(messageKey);
        this.status = status;
        this.args = args;
    }

    public String getMessageKey() {
        return getMessage();
    }

    public Object[] getArgs() {
        return args;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
