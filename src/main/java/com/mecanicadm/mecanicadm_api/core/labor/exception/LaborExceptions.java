package com.mecanicadm.mecanicadm_api.core.labor.exception;

import com.mecanicadm.mecanicadm_api.shared.exception.DomainExceptionCore;

public class LaborExceptions {

    private LaborExceptions() {
    }

    public static class LaborNotFound extends DomainExceptionCore {
        public LaborNotFound() {
            super("labor.not.found", 404);
        }
    }
}
