package com.stefanski.loan.core.error;

/**
 * Throw when resource (customer, loan) was not found.
 *
 * @author Dariusz Stefanski
 */
public class ResourceNotFoundException extends Exception {

    public ResourceNotFoundException() {
        super();
    }

    public ResourceNotFoundException(String message) {
        super(message);
    }

    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
