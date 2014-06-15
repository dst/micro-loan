package com.stefanski.loan.rest.model.response;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import org.springframework.http.HttpStatus;

import static com.fasterxml.jackson.databind.annotation.JsonSerialize.Inclusion.NON_NULL;

/**
 * Error response compatible with Spring Actuator.
 *
 * @author Dariusz Stefanski
 */
@Data
public class ErrorResp {

    public static final String INVALID_PARAM_ERR = "Invalid parameter";
    public static final String INVALID_TYPE_ERR = "Invalid type of parameter";
    public static final String RESOURCE_NOT_FOUND_ERR = "Resource does not exist";
    public static final String RISK_TOO_HIGH_ERR = "Risk of applying load is too high";

    private String error;
    private String message;
    private int status;
    private long timestamp;

    public ErrorResp(String error, String message, HttpStatus status) {
        this.error = error;
        this.message = message;
        this.status = status.value();
        this.timestamp = System.currentTimeMillis();
    }
}
