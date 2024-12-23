package com.buck.vsplay.global.exception;

import jakarta.validation.ConstraintViolation;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.Set;

@Getter
@AllArgsConstructor
public class ConstraintViolationError {

    private String propertyPath;
    private String invalidValue;
    private String message;

    public static List<ConstraintViolationError> of(Set<ConstraintViolation<?>> constraintViolations) {
        return constraintViolations.stream()
                .map(constraintViolation -> new ConstraintViolationError(
                        constraintViolation.getPropertyPath().toString(),
                        constraintViolation.getInvalidValue() != null ? constraintViolation.getInvalidValue().toString() : "null",
                        constraintViolation.getMessage()
                )).toList();
    }

}
