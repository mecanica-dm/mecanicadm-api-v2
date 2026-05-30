package com.mecanicadm.mecanicadm_api.core.client.exception;

import com.mecanicadm.mecanicadm_api.core.shared.exception.DomainExceptionCore;
import org.springframework.http.HttpStatus;

public class ClientExceptions {

    private ClientExceptions() {
    }

    public static class NotFound extends DomainExceptionCore {
        public NotFound() {
            super("client.not.found", HttpStatus.NOT_FOUND);
        }
    }

    public static class DocumentExists extends DomainExceptionCore {
        public DocumentExists() {
            super("client.document.exists", HttpStatus.CONFLICT);
        }
    }

    public static class EmailExists extends DomainExceptionCore {
        public EmailExists() {
            super("client.email.exists", HttpStatus.CONFLICT);
        }
    }

    public static class NameNotEmpty extends DomainExceptionCore {
        public NameNotEmpty() {
            super("client.name.not.empty", HttpStatus.BAD_REQUEST);
        }
    }

    public static class EmailNotEmpty extends DomainExceptionCore {
        public EmailNotEmpty() {
            super("client.email.not.empty", HttpStatus.BAD_REQUEST);
        }
    }

    public static class DocumentNotEmpty extends DomainExceptionCore {
        public DocumentNotEmpty() {
            super("client.document.not.empty", HttpStatus.BAD_REQUEST);
        }
    }
}
