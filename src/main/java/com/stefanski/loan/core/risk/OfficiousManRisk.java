package com.stefanski.loan.core.risk;

import com.stefanski.loan.core.domain.Loan;
import com.stefanski.loan.core.repository.LoanRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Dariusz Stefanski
 */
@Slf4j
@Component
public class OfficiousManRisk implements Risk {
// officious- nadgorliwy, natretny, narzucajacy sie

    @Value("${system.loan.limitPerIp}")
    private int loanLimitPerIp;

    @Autowired
    private LoanRepository loanRepository;

    @Override
    public boolean isApplicableTo(Loan loan) {
        List<Loan> loansByIp = loanRepository.findByIp(loan.getIp());
        //TODO(dst), 6/9/14: create query in repository
        int count = 0;
        for (Loan l : loansByIp) {
            if (loan.getStartDate().equals(l.getStartDate())) {
                count++;
                if (count >= loanLimitPerIp) {
                    log.info("Too many loans taken in one day from IP {}", loan.getIp());
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public String getMessage() {
        return String.format("Reached max applications (%d) per day from a single IP", loanLimitPerIp);
    }

    @Override
    public String getName() {
        return "Officious Man Risk";
    }

    public void setLoanLimitPerIp(int loanLimitPerIp) {
        this.loanLimitPerIp = loanLimitPerIp;
    }
}
