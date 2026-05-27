package com.mecanicadm.mecanicadm_api.core.vehicle.exception;

import com.mecanicadm.mecanicadm_api.infra.exception.DomainException;
import org.springframework.http.HttpStatus;

public class VehicleExceptions {

    private VehicleExceptions() {
    }

    public static class NotFound extends DomainException {
        public NotFound() {
            super("vehicle.not.found", HttpStatus.NOT_FOUND);
        }
    }

    public static class VehicleExists extends DomainException {
        public VehicleExists() {
            super("vehicle.licensePlate.exists", HttpStatus.BAD_REQUEST);
        }
    }
}
