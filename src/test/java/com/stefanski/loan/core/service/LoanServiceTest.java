package com.stefanski.loan.core.service;

import com.stefanski.loan.core.domain.Loan;
import com.stefanski.loan.core.ex.ResourceNotFoundException;
import com.stefanski.loan.core.repository.LoanRepository;
import com.stefanski.loan.core.risk.RiskAnalyser;
import com.stefanski.loan.rest.model.request.LoanRequest;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static com.stefanski.loan.util.TestDataFixture.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

/**
 * @author Dariusz Stefanski
 */
public class LoanServiceTest {

    @Mock
    private CustomerService customerService;

    @Mock
    private RiskAnalyser riskAnalyser;

    @Mock
    private LoanRepository loanRepository;

    @InjectMocks
    private LoanService loanService;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void shouldFindLoanIfExists() throws Exception {
        // given:
        Loan loan = new Loan();
        loan.setId(LOAN_ID);
        when(loanRepository.findOne(LOAN_ID)).thenReturn(loan);

        // when:
        Loan foundLoan = loanService.findById(LOAN_ID);

        // then:
        assertThat(foundLoan).isNotNull();
        assertThat(foundLoan.getId()).isEqualTo(LOAN_ID);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void shouldThrowExceptionWhenFindingNotExitstingLoan() throws Exception {
        // given:
        when(loanRepository.findOne(LOAN_ID)).thenReturn(null);

        // when:
        loanService.findById(LOAN_ID);

        // then:
        // exception should be thrown
    }


    @Test
    public void shouldCreateLoanForValidRequest() throws Exception {
        // given:
        LoanRequest loanReq = simpleLoanReqest();
        Loan loan = new Loan();
        loan.setId(LOAN_ID);
        when(loanRepository.save(any(Loan.class))).thenReturn(loan);

        // when:
        Long loanId = loanService.applyForLoan(CUSTOMER_ID, loanReq);

        // then:
        assertThat(loanId).isEqualTo(LOAN_ID);
    }
}