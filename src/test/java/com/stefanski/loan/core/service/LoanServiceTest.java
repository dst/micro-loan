package com.stefanski.loan.core.service;

import com.stefanski.loan.core.domain.Customer;
import com.stefanski.loan.core.domain.Extension;
import com.stefanski.loan.core.domain.Loan;
import com.stefanski.loan.core.ex.ResourceNotFoundException;
import com.stefanski.loan.core.repository.ExtensionRepository;
import com.stefanski.loan.core.repository.LoanRepository;
import com.stefanski.loan.core.risk.RiskAnalyser;
import com.stefanski.loan.rest.model.request.LoanReq;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static com.stefanski.loan.util.TestDataFixture.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
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

    @Mock
    private ExtensionRepository extensionRepository;

    @InjectMocks
    private LoanService loanService;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        loanService.setExtensionDays(7);
        loanService.setExtensionInterest(new BigDecimal("1.5"));
        loanService.setLoanInterest(new BigDecimal("7.2"));
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
    public void shouldThrowExceptionWhenFindingNotExitstingLoan() throws Exception {
        // given:
        when(loanRepository.findOne(LOAN_ID)).thenReturn(null);

        // when:
        loanService.findLoanById(LOAN_ID);

        // then:
        // exception should be thrown
    }

    @Test
    public void shouldFindExtensionIfExists() throws Exception {
        // given:
        Extension ext = new Extension();
        ext.setId(EXTENSION_ID);
        Loan loan = simpleLoan();
        loan.setId(LOAN_ID);
        ext.setLoan(loan);
        when(extensionRepository.findOne(EXTENSION_ID)).thenReturn(ext);

        // when:
        Extension foundExt = loanService.findLoanExtension(LOAN_ID, EXTENSION_ID);

        // then:
        assertThat(foundExt).isNotNull();
        assertThat(foundExt.getId()).isEqualTo(EXTENSION_ID);
    }


    @Test(expected = ResourceNotFoundException.class)
    public void shouldThrowExceptionWhenSearchingNotExistingExtension() throws Exception {
        // given:
        when(extensionRepository.findOne(EXTENSION_ID)).thenReturn(null);

        // when:
        loanService.findLoanExtension(LOAN_ID, EXTENSION_ID);

        // then:
        // exception should be thrown
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

    @Test
    public void shouldCreateExtension() throws Exception {
        // given:
        Loan loan = simpleLoan();
        loan.setId(LOAN_ID);
        when(loanRepository.findOne(LOAN_ID)).thenReturn(loan);

        Extension extension = simpleExtension();
        extension.setId(EXTENSION_ID);
        when(extensionRepository.save(any(Extension.class))).thenReturn(extension);

        // when:
        Long extensionId = loanService.extendLoan(LOAN_ID);

        // then:
        assertThat(extensionId).isEqualTo(EXTENSION_ID);
    }

    @Test
    public void shouldIncreaseLoanInterestAfterExtending() throws Exception {
        // given:
        BigDecimal interest = new BigDecimal("12.13");
        Loan loan = simpleLoan();
        loan.setInterest(interest);
        loan.setId(LOAN_ID);
        when(loanRepository.findOne(LOAN_ID)).thenReturn(loan);

        Extension extension = simpleExtension();
        extension.setId(EXTENSION_ID);
        when(extensionRepository.save(any(Extension.class))).thenReturn(extension);

        // when:
        loanService.extendLoan(LOAN_ID);

        // then:
        ArgumentCaptor<Loan> loanCapture = ArgumentCaptor.forClass(Loan.class);
        verify(loanRepository).save(loanCapture.capture());
        BigDecimal newInterest = loanCapture.getValue().getInterest();
        assertThat(newInterest).isGreaterThan(interest);
    }

    @Test
    public void shouldIncreaseLoanTermAfterExtending() throws Exception {
        // given:
        LocalDateTime end = LocalDateTime.now();
        Loan loan = simpleLoan();
        loan.setEnd(end);
        loan.setId(LOAN_ID);
        when(loanRepository.findOne(LOAN_ID)).thenReturn(loan);

        Extension extension = simpleExtension();
        extension.setId(EXTENSION_ID);
        when(extensionRepository.save(any(Extension.class))).thenReturn(extension);

        // when:
        loanService.extendLoan(LOAN_ID);

        // then:
        ArgumentCaptor<Loan> loanCapture = ArgumentCaptor.forClass(Loan.class);
        verify(loanRepository).save(loanCapture.capture());
        LocalDateTime newDeadline = loanCapture.getValue().getEnd();
        assertThat(newDeadline.isAfter(end)).isTrue();
    }
}
