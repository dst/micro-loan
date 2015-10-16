package com.stefanski.loan.core.risk;

import com.stefanski.loan.core.domain.Loan;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Dariusz Stefanski
 */
@Slf4j
@Component
public class RiskAnalyser {

    private List<Risk> rules;

    @Autowired
    public RiskAnalyser(List<Risk> rules) {
        this.rules = rules;
    }

    /**
     * Validates a given loan according to specified rules.
     *
     * @param loan validated loan
     * @throws RiskTooHighException if at least one risk was detected
     */
    public void validate(Loan loan) {
        for (Risk rule : rules) {
            log.debug("Checking {}", rule.getName());
            if (rule.isApplicableTo(loan)) {
                throw new RiskTooHighException(rule.getMessage());
            }
        }
    }
}
