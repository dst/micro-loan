package com.stefanski.loan.core.risk

import com.stefanski.loan.core.domain.Loan
import spock.lang.Specification

import static com.stefanski.loan.util.TestDataFixture.*
import static java.math.BigDecimal.ONE
import static java.math.BigDecimal.TEN

/**
 * @author Dariusz Stefanski
 */
class DrunkManAtNightRiskSpec extends Specification {

    private static final BigDecimal MAX_AMOUNT = TEN;

    private DrunkManAtNightRisk risk;

    def setup() {
        risk = new DrunkManAtNightRisk();
        risk.setMaxAmount(MAX_AMOUNT);
    }

    def "Detect risk of drunk man at night when needed"() {
        given:
        Loan loan = createLoan(amount, time);

        expect:
        risk.isApplicableTo(loan) == risky

        where:
        time    | amount       | risky
        day()   | belowLimit() | false
        day()   | limit()      | false
        day()   | aboveLimit() | false
        night() | belowLimit() | false
        night() | limit()      | true
        night() | aboveLimit() | true
    }

    private BigDecimal limit() {
        MAX_AMOUNT
    }

    private BigDecimal belowLimit() {
        MAX_AMOUNT.subtract(ONE);
    }

    private BigDecimal aboveLimit() {
        MAX_AMOUNT.add(ONE);
    }
}