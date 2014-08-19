package com.stefanski.loan.core.service;

import com.stefanski.loan.core.domain.Customer;
import com.stefanski.loan.core.domain.Loan;
import com.stefanski.loan.core.ex.ResourceNotFoundException;
import com.stefanski.loan.core.repository.ExtensionRepository;
import com.stefanski.loan.core.repository.LoanRepository;
import com.stefanski.loan.core.risk.RiskAnalyser;
import com.stefanski.loan.rest.model.request.LoanReq;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static com.stefanski.loan.util.TestDataFixture.CUSTOMER_ID;
import static com.stefanski.loan.util.TestDataFixture.LOAN_ID;
import static com.stefanski.loan.util.TestDataFixture.simpleCustomer;
import static com.stefanski.loan.util.TestDataFixture.simpleLoanReqest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

/**
 * @author Dariusz Stefanski
 */
public class LoanServiceTest {

    private static final BigDecimal LOAN_INTEREST = new BigDecimal("7.2");

    @Mock
    private CustomerService customerService;

    @Mock
    private RiskAnalyser riskAnalyser;

    @Mock
    private LoanRepository loanRepository;

    @Mock
    private ExtensionRepository extensionRepository;

    private LoanService loanService;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        loanService = new LoanService(LOAN_INTEREST, customerService, loanRepository, riskAnalyser);
    }

    @Test
    public void shouldFindLoanIfExists() throws Exception {
        // given:
        Loan loan = new Loan();
        loan.setId(LOAN_ID);
        when(loanRepository.findOne(LOAN_ID)).thenReturn(loan);

        // when:
        Loan foundLoan = loanService.findLoanById(LOAN_ID);

        // then:
        assertThat(foundLoan).isNotNull();
        assertThat(foundLoan.getId()).isEqualTo(LOAN_ID);
    }

    @Test
    public void shouldFindCustomerLoans() throws Exception {
        // given:
        Customer customer = simpleCustomer();
        customer.setLoans(Arrays.asList(new Loan(), new Loan()));
        when(customerService.findById(CUSTOMER_ID)).thenReturn(customer);

        // when:
        List<Loan> loans = loanService.findCustomerLoans(CUSTOMER_ID);

        // then:
        assertThat(loans).hasSize(2);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void shouldThrowExceptionWhenSearchingCustomerLoansForNullId() throws Exception {
        // when:
        loanService.findCustomerLoans(null);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void shouldThrowExceptionWhenSearchingNotExitstingLoan() throws Exception {
        // given:
        when(loanRepository.findOne(LOAN_ID)).thenReturn(null);

        // when:
        loanService.findLoanById(LOAN_ID);
    }

    @Test
    public void shouldCreateLoanForValidRequest() throws Exception {
        // given:
        LoanReq loanReq = simpleLoanReqest();
        Loan loan = new Loan();
        loan.setId(LOAN_ID);
        when(loanRepository.save(any(Loan.class))).thenReturn(loan);

        // when:
        Long loanId = loanService.applyForLoan(loanReq);

        // then:
        assertThat(loanId).isEqualTo(LOAN_ID);
    }
}
