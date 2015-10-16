package com.stefanski.loan.core.risk

import com.stefanski.loan.core.repository.LoanRepository
import spock.lang.Specification

import static com.stefanski.loan.util.TestDataFixture.simpleLoan

/**
 * @author Dariusz Stefanski
 */
class OfficiousManRiskSpec extends Specification {

    def "should detect risk when there is too many loans issued from a single IP in one day"() {
        given:
            def loanLimitPerIp = 2
            LoanRepository loanRepository = Mock(LoanRepository)
            loanRepository.loanCountFor(_, _) >> loanCount
            def risk = new OfficiousManRisk(loanLimitPerIp, loanRepository)
        expect:
            risk.isApplicableTo(simpleLoan()) == risky
        where:
            loanCount | risky
            0L        | false
            1L        | false
            2L        | true
            3L        | true
    }
}
