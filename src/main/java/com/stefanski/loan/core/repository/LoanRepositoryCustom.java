package com.stefanski.loan.core.repository;

import java.time.LocalDate;

/**
 * @author Dariusz Stefanski
 */
public interface LoanRepositoryCustom {

    long getLoanCountFor(String ip, LocalDate applicationDay);
}
