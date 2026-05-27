package com.mecanicadm.mecanicadm_api.core.stockmovements.exception;

import com.mecanicadm.mecanicadm_api.infra.exception.DomainException;
import org.springframework.http.HttpStatus;

public class StockMovementsExceptions {

    private StockMovementsExceptions() {
    }

    public static class NotFound extends DomainException {
        public NotFound() {
            super("stock.not.found", HttpStatus.NOT_FOUND);
        }
    }

    public static class InvalidQuantity extends DomainException {
        public InvalidQuantity() {
            super("stock.quantity.invalid", HttpStatus.BAD_REQUEST);
        }
    }

    public static class InsufficientStock extends DomainException {
        public InsufficientStock() {
            super("stock.quantity.insufficient", HttpStatus.BAD_REQUEST);
        }
    }
}
