package com.stefanski.loan.core.repository;

import com.stefanski.loan.core.domain.Loan;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * @author Dariusz Stefanski
 */
public interface LoanRepository extends CrudRepository<Loan, Long> {

    List<Loan> findByIp(String ip);
}
