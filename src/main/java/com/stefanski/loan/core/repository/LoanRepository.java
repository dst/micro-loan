package com.stefanski.loan.core.repository;

import com.stefanski.loan.core.domain.Loan;
import org.springframework.data.repository.CrudRepository;

/**
 * @author Dariusz Stefanski
 */
public interface LoanRepository extends CrudRepository<Loan, Long> {
}
