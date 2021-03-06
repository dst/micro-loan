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

    private static final BigDecimal MAX_AMOUNT = TEN

    def "should detect risk of drunk man at night"() {
        given:
            def risk = new DrunkManAtNightRisk(MAX_AMOUNT)
            Loan loan = simpleLoan(amount, time)
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

    private BigDecimal limit() {
        MAX_AMOUNT
    }

    private BigDecimal belowLimit() {
        MAX_AMOUNT.subtract(ONE)
    }

    private BigDecimal aboveLimit() {
        MAX_AMOUNT.add(ONE)
    }
}