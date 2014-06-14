package com.stefanski.loan.core.risk;

import com.stefanski.loan.core.domain.Loan;

/**
 * @author Dariusz Stefanski
 */
interface Risk {

    /**
     * Checks if this risk is applicable to given loan.
     *
     * @param loan
     * @return true if risk is applicable
     */
    boolean isApplicableTo(Loan loan);

    /**
     * @return message displayed to customer
     */
    default String getMessage() {
        return "Risk was detected";
    }

    /**
     * @return name of risk
     */
    default String getName() {
        return getClass().getSimpleName();
    }
}
