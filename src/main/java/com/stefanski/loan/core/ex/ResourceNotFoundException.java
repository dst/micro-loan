package com.stefanski.loan.core.ex;

/**
 * Thrown when resource (customer, loan) was not found.
 *
 * @author Dariusz Stefanski
 */
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException() {
        super();
    }

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
