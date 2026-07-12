package com.mecanicadm.mecanicadm_api.shared.validation;

import org.springframework.data.domain.Sort;

import java.util.Set;

public final class SortValidator {

    private SortValidator() {
    }

    public static Sort safeSort(String sortBy, String direction, Set<String> allowedFields, String defaultField) {
        String safeField = allowedFields.contains(sortBy) ? sortBy : defaultField;
        Sort.Direction safeDirection = parseDirection(direction);
        return Sort.by(safeDirection, safeField);
    }

    public static Sort safeSort(java.util.List<SortField> sortFields, Set<String> allowedFields, String defaultField) {
        var orders = sortFields.stream()
                .map(sf -> new Sort.Order(parseDirection(sf.direction()), allowedFields.contains(sf.field()) ? sf.field() : defaultField))
                .toList();
        return orders.isEmpty() ? Sort.by(Sort.Direction.ASC, defaultField) : Sort.by(orders);
    }

    private static Sort.Direction parseDirection(String direction) {
        if (direction == null || direction.isBlank()) {
            return Sort.Direction.ASC;
        }
        try {
            return Sort.Direction.fromString(direction.toUpperCase());
        } catch (IllegalArgumentException e) {
            return Sort.Direction.ASC;
        }
    }

    public record SortField(String field, String direction) {
    }
}
