package com.stefanski.loan.core.risk

import com.stefanski.loan.core.domain.Loan
import groovy.util.logging.Slf4j
import spock.lang.Specification

import static com.stefanski.loan.util.TestDataFixture.*
import static java.math.BigDecimal.ONE
import static java.math.BigDecimal.TEN

/**
 * @author Dariusz Stefanski
 */
@Slf4j
class DrunkManAtNightRiskSpec extends Specification {

    private static final BigDecimal MAX_AMOUNT = TEN;

    private DrunkManAtNightRisk risk;

    def setup() {
        risk = new DrunkManAtNightRisk();
        risk.setMaxAmount(MAX_AMOUNT);
    }

    def "Should detect risk of drunk man at night when needed"() {
        log.debug("Checking for time=$time and amount=$amount")

        given:
        Loan loan = simpleLoan(amount, time);

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
        hour(0) | limit()      | true
        hour(6) | limit()      | true
    }

    def "Should have message"() {
        given:
        def msg = risk.getMessage()

        expect:
        msg != null
        msg != ""
    }

    def "Should have name"() {
        given:
        def name = risk.getName()

        expect:
        name != null
        name != ""
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