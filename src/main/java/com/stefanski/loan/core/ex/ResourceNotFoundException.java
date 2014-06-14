package com.stefanski.loan.core.ex;

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
}
