package com.mecanicadm.mecanicadm_api.core.user.exception;

import com.mecanicadm.mecanicadm_api.infra.exception.DomainException;
import org.springframework.http.HttpStatus;

public class UserExceptions {

    private UserExceptions() {
    }

    public static class NotFound extends DomainException {
        public NotFound() {
            super("user.not.found", HttpStatus.NOT_FOUND);
        }
    }

    public static class EmailExists extends DomainException {
        public EmailExists() {
            super("user.email.exists", HttpStatus.CONFLICT);
        }
    }

    public static class EmailNotEmpty extends DomainException {
        public EmailNotEmpty() {
            super("user.email.not.empty", HttpStatus.BAD_REQUEST);
        }
    }

    public static class PasswordMinLength extends DomainException {
        public PasswordMinLength() {
            super("user.password.min.length", HttpStatus.BAD_REQUEST);
        }
    }

    public static class CurrentPasswordRequired extends DomainException {
        public CurrentPasswordRequired() {
            super("user.password.current.required", HttpStatus.BAD_REQUEST);
        }
    }

    public static class BadCredentials extends DomainException {
        public BadCredentials() {
            super("error.bad.credentials", HttpStatus.UNAUTHORIZED);
        }
    }

    public static class TokenInvalid extends DomainException {
        public TokenInvalid() {
            super("token.invalid", HttpStatus.BAD_REQUEST);
        }
    }

    public static class TokenExpired extends DomainException {
        public TokenExpired() {
            super("token.expired", HttpStatus.BAD_REQUEST);
        }
    }
}
