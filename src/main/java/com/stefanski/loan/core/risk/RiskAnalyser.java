package com.stefanski.loan.core.risk;

import com.stefanski.loan.core.domain.Loan;
import com.stefanski.loan.core.ex.RiskTooHighException;
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

    @Autowired
    List<Risk> rules;

    public void validate(Loan loan) throws RiskTooHighException {
        for (Risk rule : rules) {
            log.debug("Checking {}", rule.getName());
            if (rule.isApplicableTo(loan)) {
                throw new RiskTooHighException(rule.getMessage());
            }
        }
    }
}
