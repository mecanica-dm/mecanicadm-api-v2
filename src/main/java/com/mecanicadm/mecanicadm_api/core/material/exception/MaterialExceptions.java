package com.mecanicadm.mecanicadm_api.core.material.exception;

import com.mecanicadm.mecanicadm_api.infra.exception.DomainException;
import org.springframework.http.HttpStatus;

public class MaterialExceptions {

    private MaterialExceptions() {
    }

    public static class MaterialNotFound extends DomainException {
        public MaterialNotFound() {
            super("material.not.found", HttpStatus.NOT_FOUND);
        }
    }
}
