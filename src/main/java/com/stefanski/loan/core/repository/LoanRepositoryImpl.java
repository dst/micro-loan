package com.stefanski.loan.core.repository;

import com.stefanski.loan.core.domain.Loan;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * @author Dariusz Stefanski
 */
@Slf4j
@Repository
public class LoanRepositoryImpl implements LoanRepositoryCustom {

    @Autowired
    private LoanRepository loanRepository;

    @Override
    public long getLoanCountFor(String ip, LocalDate applicationDay) {
        //TODO(dst), 6/11/14: find bug
//        LocalDateTime start = applicationDay.atStartOfDay();
//        LocalDateTime end = start.plusDays(1).minusNanos(1);
//        log.debug("Searching loans between {} and {} taken from IP {}", start, end, ip);
//        return loanRepository.findByIpAndApplicationTimeBetween(ip, start, end).size();

        //TODO(dst), 6/11/14: fix above
        List<Loan> loans = loanRepository.findByIp(ip);
        return loans.stream().filter(
                loan -> applicationDay.equals(loan.getApplicationTime().toLocalDate())
        ).count();
    }
}
