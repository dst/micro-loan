package com.stefanski.loan.core.risk

import com.stefanski.loan.core.domain.Loan
import spock.lang.Specification

import static com.stefanski.loan.util.TestDataFixture.simpleLoan

/**
 * @author Dariusz Stefanski
 */
class RiskAnalyserSpec extends Specification {

    def "should throw exception when the rule applies"() {
        given:
            def analyzer = new RiskAnalyser([new SureRisk()])
        when:
            analyzer.validate(simpleLoan())
        then:
            thrown(RiskTooHighException)
    }

    def "should risk validation pass when none of rules applies"() {
        given:
            def analyzer = new RiskAnalyser([new NoRisk(), new NoRisk()])
        when:
            analyzer.validate(simpleLoan())
        then:
            notThrown(RiskTooHighException)
    }

    private class NoRisk implements Risk {
        @Override
        boolean isApplicableTo(Loan loan) {
            return false
        }
    }

    private class SureRisk implements Risk {
        @Override
        boolean isApplicableTo(Loan loan) {
            return true
        }
    }
}
