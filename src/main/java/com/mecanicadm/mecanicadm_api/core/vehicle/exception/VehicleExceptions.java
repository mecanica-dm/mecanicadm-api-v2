package com.mecanicadm.mecanicadm_api.core.vehicle.exception;

import com.mecanicadm.mecanicadm_api.core.shared.exception.DomainExceptionCore;
import org.springframework.http.HttpStatus;

public final class VehicleExceptions {

    public static class LicensePlateNotEmpty extends DomainExceptionCore {
        public LicensePlateNotEmpty() { super("validation.vehicle.licensePlate.not.blank", HttpStatus.BAD_REQUEST); }
    }

    public static class InvalidLicensePlate extends DomainExceptionCore {
        public InvalidLicensePlate() { super("validation.vehicle.licensePlate.invalid", HttpStatus.BAD_REQUEST); }
    }

    public static class ModelNotEmpty extends DomainExceptionCore {
        public ModelNotEmpty() { super("validation.vehicle.model.not.blank", HttpStatus.BAD_REQUEST); }
    }

    public static class BrandNotEmpty extends DomainExceptionCore {
        public BrandNotEmpty() { super("validation.vehicle.brand.not.blank", HttpStatus.BAD_REQUEST); }
    }

    public static class InvalidModelYear extends DomainExceptionCore {
        public InvalidModelYear() { super("validation.vehicle.modelYear.invalid", HttpStatus.BAD_REQUEST); }
    }

    public static class NotFound extends DomainExceptionCore {
        public NotFound() { super("vehicle.not.found", HttpStatus.NOT_FOUND); }
    }

    public static class VehicleExists extends DomainExceptionCore {
        public VehicleExists(String licensePlate) { super("vehicle.exists", HttpStatus.BAD_REQUEST, licensePlate); }
        public VehicleExists() { super("vehicle.licensePlate.exists", HttpStatus.BAD_REQUEST); }
    }
}

