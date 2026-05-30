package com.mecanicadm.mecanicadm_api.core.material.exception;

import com.mecanicadm.mecanicadm_api.core.shared.exception.DomainExceptionCore;
import org.springframework.http.HttpStatus;

public class MaterialExceptions {

    private MaterialExceptions() {
    }

    public static class MaterialNotFound extends DomainExceptionCore {
        public MaterialNotFound() {
            super("material.not.found", HttpStatus.NOT_FOUND);
        }
    }
}
