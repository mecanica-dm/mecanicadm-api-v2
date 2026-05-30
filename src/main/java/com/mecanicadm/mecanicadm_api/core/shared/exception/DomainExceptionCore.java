package com.mecanicadm.mecanicadm_api.core.shared.exception;

import org.springframework.http.HttpStatus;

public abstract class DomainExceptionCore extends RuntimeException {
    private final transient Object[] args;
    private final HttpStatus status;

    protected DomainExceptionCore(String messageKey, HttpStatus status, Object... args) {
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

