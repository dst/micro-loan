package com.stefanski.loan.rest.error;

import lombok.Data;
import org.springframework.validation.FieldError;

/**
 * @author Dariusz Stefanski
 */
@Data
public class ParameterError {
    private String field;
    private String message;

    public ParameterError() {
    }

    public ParameterError(String field, String message) {
        this.field = field;
        this.message = message;
    }

    public static ParameterError fromFieldError(FieldError error) {
        return new ParameterError(error.getField(), error.getDefaultMessage());
    }
}
