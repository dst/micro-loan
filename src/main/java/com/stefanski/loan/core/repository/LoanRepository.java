package com.stefanski.loan.core.repository;

import com.stefanski.loan.core.domain.Loan;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Dariusz Stefanski
 */
public interface LoanRepository extends CrudRepository<Loan, Long>, LoanRepositoryCustom {

    long countByIpAndStartBetween(String ip, LocalDateTime start, LocalDateTime end);
}
