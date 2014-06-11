package com.stefanski.loan.core.risk

import com.stefanski.loan.core.domain.Loan
import com.stefanski.loan.core.ex.RiskTooHighException
import spock.lang.Specification

import static com.stefanski.loan.util.TestDataFixture.simpleLoan

/**
 * @author Dariusz Stefanski
 */
class RiskAnalyserSpec extends Specification {

    def "Should throw exception when some rule applies"() {
        given:
        def analyzer = new RiskAnalyser()
        analyzer.setRules([new SureRisk()])

        when:
        analyzer.validate(simpleLoan())

        then:
        thrown(RiskTooHighException)
    }

    def "Should risk validation pass when none of rules applies"() {
        given:
        def analyzer = new RiskAnalyser()
        analyzer.setRules([new NoRisk(), new NoRisk()])

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

        @Override
        String getMessage() {
            return null
        }

        @Override
        String getName() {
            return null
        }
    }

    private class SureRisk implements Risk {
        @Override
        boolean isApplicableTo(Loan loan) {
            return true;
        }

        @Override
        String getMessage() {
            return null
        }

        @Override
        String getName() {
            return null
        }
    }
}
