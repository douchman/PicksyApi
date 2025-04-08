package com.buck.vsplay.global.constants;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Order;
import jakarta.persistence.criteria.Path;

public enum OrderType {
    DESC, ASC, NONE;

    public Order convertOrderTypeToJpaOrder(CriteriaBuilder criteriaBuilder, Path<?> path){
        return switch (this){
            case ASC -> criteriaBuilder.asc(path);
            case DESC -> criteriaBuilder.desc(path);
            case NONE -> null;
        };
    }
}
