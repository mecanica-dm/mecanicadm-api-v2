package com.mecanicadm.mecanicadm_api.core.stockmovements.exception;

import com.mecanicadm.mecanicadm_api.core.shared.exception.DomainExceptionCore;
import org.springframework.http.HttpStatus;

public class StockMovementsExceptions {

    private StockMovementsExceptions() {
    }

    public static class NotFound extends DomainExceptionCore {
        public NotFound() {
            super("stock.not.found", HttpStatus.NOT_FOUND);
        }
    }

    public static class InvalidQuantity extends DomainExceptionCore {
        public InvalidQuantity() {
            super("stock.quantity.invalid", HttpStatus.BAD_REQUEST);
        }
    }

    public static class InsufficientStock extends DomainExceptionCore {
        public InsufficientStock() {
            super("stock.quantity.insufficient", HttpStatus.BAD_REQUEST);
        }
    }
}
