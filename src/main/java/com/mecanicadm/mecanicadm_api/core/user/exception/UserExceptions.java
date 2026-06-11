package com.mecanicadm.mecanicadm_api.core.user.exception;

import com.mecanicadm.mecanicadm_api.shared.exception.DomainExceptionCore;
import org.springframework.http.HttpStatus;

public class UserExceptions {

    private UserExceptions() {
    }

    public static class NotFound extends DomainExceptionCore {
        public NotFound() {
            super("user.not.found", HttpStatus.NOT_FOUND);
        }
    }

    public static class UserAlreadyExists extends DomainExceptionCore {
        public UserAlreadyExists() {
            super("user.already.exists", HttpStatus.CONFLICT);
        }
    }

    public static class EmailExists extends DomainExceptionCore {
        public EmailExists() {
            super("user.email.exists", HttpStatus.CONFLICT);
        }
    }

    public static class EmailNotEmpty extends DomainExceptionCore {
        public EmailNotEmpty() {
            super("user.email.not.empty", HttpStatus.BAD_REQUEST);
        }
    }

    public static class PasswordMinLength extends DomainExceptionCore {
        public PasswordMinLength() {
            super("user.password.min.length", HttpStatus.BAD_REQUEST);
        }
    }

    public static class CurrentPasswordRequired extends DomainExceptionCore {
        public CurrentPasswordRequired() {
            super("user.password.current.required", HttpStatus.BAD_REQUEST);
        }
    }

    public static class BadCredentials extends DomainExceptionCore {
        public BadCredentials() {
            super("error.bad.credentials", HttpStatus.UNAUTHORIZED);
        }
    }

    public static class TokenInvalid extends DomainExceptionCore {
        public TokenInvalid() {
            super("token.invalid", HttpStatus.BAD_REQUEST);
        }
    }

    public static class TokenExpired extends DomainExceptionCore {
        public TokenExpired() {
            super("token.expired", HttpStatus.BAD_REQUEST);
        }
    }
}
