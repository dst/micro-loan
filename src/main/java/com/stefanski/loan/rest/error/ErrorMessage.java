package com.stefanski.loan.rest.error;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

import static com.fasterxml.jackson.databind.annotation.JsonSerialize.Inclusion.NON_NULL;

/**
 * @author Dariusz Stefanski
 */
@Data
@JsonSerialize(include = NON_NULL)
public class ErrorMessage {

    public static final String INTERNAL_ERROR_MSG = "System internal error";
    public static final String INVALID_PARAMETERS_MSG = "Invalid parameters";
    public static final String INVALID_TYPE_MSG = "Invalid type of parameter";
    public static final String RESOURCE_NOT_FOUND_MSG = "Resource does not exist";
    public static final String RISK_TOO_HIGH_MSG = "Risk of applyging load is too high";

    private String message;

    // It is optional
    private Object details;

    public ErrorMessage(String message) {
        this.message = message;
    }
}
