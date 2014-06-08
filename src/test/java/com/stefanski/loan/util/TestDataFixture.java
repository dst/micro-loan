package com.stefanski.loan.util;

import com.stefanski.loan.core.domain.Customer;
import com.stefanski.loan.core.domain.Loan;
import com.stefanski.loan.rest.model.request.LoanRequest;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author Dariusz Stefanski
 */
public class TestDataFixture {

    public static final Long CUSTOMER_ID = 123L;
    public static final Long LOAN_ID = 999L;

    public static final String SIMPLE_FIRST_NAME = "John";
    public static final String SIMPLE_LAST_NAME = "Smith";


    public static Customer simpleCustomer() {
        Customer customer = new Customer();
        customer.setFirstName(SIMPLE_FIRST_NAME);
        customer.setLastName(SIMPLE_LAST_NAME);
        return customer;
    }

    public static Customer polishCustomer() {
        Customer customer = new Customer();
        customer.setFirstName("Mirosław");
        customer.setLastName("Żółw");
        return customer;
    }

    public static Loan simpleLoan() {
        return simpleLoanWithAmount(BigDecimal.TEN);
    }

    public static Loan simpleLoanWithAmount(BigDecimal amount) {
        Loan loan = new Loan();
        loan.setAmount(amount);
        loan.setInterest(BigDecimal.ONE);
        loan.setStartDateTime(LocalDateTime.now());
        loan.setEndDateTime(LocalDateTime.now());
        return loan;
    }

    public static LoanRequest simpleLoanReqest() {
        LoanRequest loanReq = new LoanRequest();
        loanReq.setAmount(BigDecimal.TEN);
        loanReq.setDaysCount(30);
        return loanReq;
    }
}
