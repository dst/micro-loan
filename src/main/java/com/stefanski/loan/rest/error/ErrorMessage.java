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

    private String message;

    // It is optional
    private Object details;

    public ErrorMessage() {
    }

    public ErrorMessage(String message) {
        this.message = message;
    }
}
