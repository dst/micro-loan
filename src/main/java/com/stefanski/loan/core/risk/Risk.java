package com.stefanski.loan.core.risk;

import com.stefanski.loan.core.domain.Loan;

/**
 * @author Dariusz Stefanski
 */
interface Risk {

    boolean isApplicableTo(Loan loan);

    String getMessage();

    String getName();
}
