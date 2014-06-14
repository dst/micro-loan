package com.stefanski.loan.util;

import com.stefanski.loan.core.domain.Customer;
import com.stefanski.loan.core.domain.Extension;
import com.stefanski.loan.core.domain.Loan;
import com.stefanski.loan.rest.model.request.LoanRequest;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static java.math.BigDecimal.TEN;

/**
 * @author Dariusz Stefanski
 */
public class TestDataFixture {

    public static final Long CUSTOMER_ID = 123L;
    public static final Long LOAN_ID = 999L;
    public static final Long EXTENSION_ID = 555L;

    public static final String SIMPLE_FIRST_NAME = "John";
    public static final String SIMPLE_LAST_NAME = "Smith";

    public static final String IP = "127.0.0.1";

    public static Customer simpleCustomer() {
        return new Customer(SIMPLE_FIRST_NAME, SIMPLE_LAST_NAME);
    }

    public static Customer polishCustomer() {
        return new Customer("Mirosław", "Żółw");
    }

    public static Loan simpleLoan() {
        return simpleLoanWithAmount(TEN);
    }

    public static Loan simpleLoanWithAmount(BigDecimal amount) {
        return simpleLoan(amount, null);
    }

    public static Loan simpleLoan(BigDecimal amount, LocalDateTime start) {
        if (start == null) {
            start = LocalDateTime.now();
        }
        LocalDateTime end = start.plusDays(30);

        Loan loan = new Loan();
        loan.setAmount(amount);
        loan.setInterest(TEN);
        loan.setStart(start);
        loan.setEnd(end);
        loan.setIp(IP);
        return loan;
    }


    public static LoanRequest simpleLoanReqest() {
        LoanRequest loanReq = new LoanRequest();
        loanReq.setAmount(TEN);
        loanReq.setDaysCount(30);
        loanReq.setIp(IP);
        return loanReq;
    }

    public static Extension simpleExtension() {
        Extension extension = new Extension();
        extension.setCreationTime(LocalDateTime.now());
        return extension;
    }

    public static LocalDateTime night() {
        return LocalDateTime.now().withHour(2);
    }

    public static LocalDateTime day() {
        return LocalDateTime.now().withHour(12);
    }

    public static LocalDateTime hour(int hours) {
        return LocalDateTime.now()
                .withHour(hours)
                .withMinute(0)
                .withSecond(0)
                .withNano(0);
    }
}
