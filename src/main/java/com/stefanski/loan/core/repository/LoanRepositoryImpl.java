package com.stefanski.loan.core.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @author Dariusz Stefanski
 */
@Slf4j
@Repository
public class LoanRepositoryImpl implements LoanRepositoryCustom {

    @Autowired
    private LoanRepository loanRepository;

    @Override
    public long loanCountFor(String ip, LocalDate day) {
        LocalDateTime start = day.atStartOfDay();
        LocalDateTime end = start.plusDays(1).minusNanos(1);
        log.debug("Searching loans between {} and {} taken from IP {}", start, end, ip);
        return loanRepository.countByIpAndStartBetween(ip, start, end);
    }
}
