package com.stefanski.loan.core.ex;

/**
 * @author Dariusz Stefanski
 */
public class RiskTooHighException extends Exception {

    public RiskTooHighException(String message) {
        super(message);
    }
}
