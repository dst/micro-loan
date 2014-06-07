package com.stefanski.loan.util;

import com.stefanski.loan.core.domain.Loan;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author Dariusz Stefanski
 */
public class TestDataFixture {

    public static final Long CUSTOMER_ID = 123L;
    public static final Long LOAN_ID = 999L;


    public static Loan simpleLoan() {
        Loan loan = new Loan();
        loan.setAmount(BigDecimal.TEN);
        loan.setInterest(BigDecimal.ONE);
        loan.setStartDateTime(LocalDateTime.now());
        loan.setEndDateTime(LocalDateTime.now());
        return loan;
    }

}
