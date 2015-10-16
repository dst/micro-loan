package com.stefanski.loan.core.repository;

import java.time.LocalDate;

/**
 * @author Dariusz Stefanski
 */
public interface LoanRepositoryCustom {

    /**
     * Get number of loans which where requested in a given day from a give IP address.
     *
     * @param ip  IP address from where loan was taken
     * @param day when loan was created
     * @return number of loans
     */
    long loanCountFor(String ip, LocalDate day);
}
