package com.stefanski.loan.core.risk;

import com.stefanski.loan.core.domain.Loan;
import com.stefanski.loan.core.repository.LoanRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

/**
 * @author Dariusz Stefanski
 */
@Slf4j
@Component
// officious- nadgorliwy, natretny, narzucajacy sie
class OfficiousManRisk implements Risk {

    private long loanLimitPerIp;

    private LoanRepository loanRepository;

    @Autowired
    public OfficiousManRisk(@Value("${system.loan.limitPerIp}") long loanLimitPerIp,
                            LoanRepository loanRepository) {
        this.loanLimitPerIp = loanLimitPerIp;
        this.loanRepository = loanRepository;
    }

    @Override
    public boolean isApplicableTo(Loan loan) {
        LocalDate startDay = loan.getStart().toLocalDate();
        long count = loanRepository.getLoanCountFor(loan.getIp(), startDay);
        log.debug("{} loans taken in one day from IP {}", count, loan.getIp());
        return count >= loanLimitPerIp;
    }

    @Override
    public String getMessage() {
        return String.format("Reached max applications (%d) per day from a single IP", loanLimitPerIp);
    }

    @Override
    public String getName() {
        return "Officious Man Risk";
    }
}
