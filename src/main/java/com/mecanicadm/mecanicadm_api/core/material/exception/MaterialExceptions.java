package com.mecanicadm.mecanicadm_api.core.material.exception;

import com.mecanicadm.mecanicadm_api.shared.exception.DomainExceptionCore;

public class MaterialExceptions {

    private MaterialExceptions() {
    }

    public static class MaterialNotFound extends DomainExceptionCore {
        public MaterialNotFound() {
            super("material.not.found", 404);
        }
    }
}
