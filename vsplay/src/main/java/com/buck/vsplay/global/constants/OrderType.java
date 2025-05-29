package com.buck.vsplay.global.constants;

import org.springframework.data.domain.Sort;

import java.util.Optional;

public enum OrderType {
    DESC, ASC, NONE;

    public Optional<Sort.Order> toSortOrder(String property) {
        return switch (this) {
            case ASC -> Optional.of(Sort.Order.asc(property));
            case DESC -> Optional.of(Sort.Order.desc(property));
            case NONE -> Optional.empty();
        };
    }
}
