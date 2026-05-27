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
            super("vehicle.license.plate.exists", HttpStatus.CONFLICT);
        }
    }

    public static class LicensePlateNotEmpty extends DomainException {
        public LicensePlateNotEmpty() {
            super("vehicle.license.plate.not.empty", HttpStatus.BAD_REQUEST);
        }
    }

    public static class InvalidLicensePlate extends DomainException {
        public InvalidLicensePlate() {
            super("vehicle.license.plate.invalid", HttpStatus.BAD_REQUEST);
        }
    }

    public static class ModelNotEmpty extends DomainException {
        public ModelNotEmpty() {
            super("vehicle.model.not.empty", HttpStatus.BAD_REQUEST);
        }
    }

    public static class BrandNotEmpty extends DomainException {
        public BrandNotEmpty() {
            super("vehicle.brand.not.empty", HttpStatus.BAD_REQUEST);
        }
    }

    public static class InvalidModelYear extends DomainException {
        public InvalidModelYear() {
            super("vehicle.model.year.invalid", HttpStatus.BAD_REQUEST);
        }
    }

    public static class ModelYearCannotBeInFuture extends DomainException {
        public ModelYearCannotBeInFuture() {
            super("vehicle.model.year.cannot.be.future", HttpStatus.BAD_REQUEST);
        }
    }
}
