package com.stefanski.loan.core.risk

import com.stefanski.loan.core.repository.LoanRepository
import org.junit.Before
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import spock.lang.Specification

import static com.stefanski.loan.util.TestDataFixture.simpleLoan

/**
 * @author Dariusz Stefanski
 */
class OfficiousManRiskSpec extends Specification {

    @Mock
    private LoanRepository loanRepository;

    @InjectMocks
    private OfficiousManRisk risk;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    def "Should detect risk when there is too many loans issued from a single IP in one day"() {
        given:
        risk.setLoanLimitPerIp(2)
        def loan = simpleLoan()
        def day = loan.getStart().toLocalDate()

        Mockito.when(loanRepository.getLoanCountFor(loan.getIp(), day)).thenReturn(loanCount)

        expect:
        risk.isApplicableTo(loan) == risky

        where:
        loanCount | risky
        0L        | false
        1L        | false
        2L        | true
        3L        | true
    }
}
