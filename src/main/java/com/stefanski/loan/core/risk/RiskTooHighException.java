package com.stefanski.loan.core.risk;

/**
 * Thrown when risk of giving a loan is too high.
 *
 * @author Dariusz Stefanski
 */
public class RiskTooHighException extends RuntimeException {

    public RiskTooHighException(String message) {
        super(message);
    }
}
