package com.mecanicadm.mecanicadm_api.core.client.exception;

import com.mecanicadm.mecanicadm_api.infra.exception.DomainException;
import org.springframework.http.HttpStatus;

public class ClientExceptions {

    private ClientExceptions() {
    }

    public static class NotFound extends DomainException {
        public NotFound() {
            super("client.not.found", HttpStatus.NOT_FOUND);
        }
    }

    public static class DocumentExists extends DomainException {
        public DocumentExists() {
            super("client.document.exists", HttpStatus.CONFLICT);
        }
    }

    public static class EmailExists extends DomainException {
        public EmailExists() {
            super("client.email.exists", HttpStatus.CONFLICT);
        }
    }

    public static class NameNotEmpty extends DomainException {
        public NameNotEmpty() {
            super("client.name.not.empty", HttpStatus.BAD_REQUEST);
        }
    }

    public static class EmailNotEmpty extends DomainException {
        public EmailNotEmpty() {
            super("client.email.not.empty", HttpStatus.BAD_REQUEST);
        }
    }

    public static class DocumentNotEmpty extends DomainException {
        public DocumentNotEmpty() {
            super("client.document.not.empty", HttpStatus.BAD_REQUEST);
        }
    }
}
