package com.stefanski.loan.core.ex;

/**
 * Thrown when risk of giving a loan is too high.
 *
 * @author Dariusz Stefanski
 */
public class RiskTooHighException extends Exception {

    public RiskTooHighException(String message) {
        super(message);
    }
}
