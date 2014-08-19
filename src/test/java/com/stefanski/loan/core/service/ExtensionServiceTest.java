package com.stefanski.loan.core.service;

import com.stefanski.loan.core.domain.Extension;
import com.stefanski.loan.core.domain.Loan;
import com.stefanski.loan.core.ex.ResourceNotFoundException;
import com.stefanski.loan.core.repository.ExtensionRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static com.stefanski.loan.util.TestDataFixture.EXTENSION_ID;
import static com.stefanski.loan.util.TestDataFixture.LOAN_ID;
import static com.stefanski.loan.util.TestDataFixture.simpleExtension;
import static com.stefanski.loan.util.TestDataFixture.simpleLoan;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Dariusz Stefanski
 */
public class ExtensionServiceTest {

    private static final int EXTENSION_DAYS = 7;
    private static final BigDecimal EXTENSION_INTEREST = new BigDecimal("1.5");

    @Mock
    private ExtensionRepository extensionRepository;

    @Mock
    private LoanService loanService;

    private ExtensionService extensionService;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        extensionService = new ExtensionService(EXTENSION_DAYS, EXTENSION_INTEREST,
                extensionRepository, loanService);
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
        Extension foundExt = extensionService.findLoanExtension(LOAN_ID, EXTENSION_ID);

        // then:
        assertThat(foundExt).isNotNull();
        assertThat(foundExt.getId()).isEqualTo(EXTENSION_ID);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void shouldThrowExceptionIfExtensionBelongsToDifferentLoan() throws Exception {
        // given:
        Extension ext = new Extension();
        ext.setId(EXTENSION_ID);
        Loan loan = simpleLoan();
        loan.setId(LOAN_ID + 1);
        ext.setLoan(loan);
        when(extensionRepository.findOne(EXTENSION_ID)).thenReturn(ext);

        // when:
        extensionService.findLoanExtension(LOAN_ID, EXTENSION_ID);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void shouldThrowExceptionWhenSearchingNotExistingExtension() throws Exception {
        // given:
        when(extensionRepository.findOne(EXTENSION_ID)).thenReturn(null);

        // when:
        extensionService.findLoanExtension(LOAN_ID, EXTENSION_ID);
    }

    @Test
    public void shouldReturnIdOfCreatedExtension() throws Exception {
        // given:
        Loan loan = simpleLoan();
        loan.setId(LOAN_ID);
        when(loanService.findLoanById(LOAN_ID)).thenReturn(loan);

        Extension extension = simpleExtension();
        extension.setId(EXTENSION_ID);
        when(extensionRepository.save(any(Extension.class))).thenReturn(extension);

        // when:
        Long extensionId = extensionService.extendLoan(LOAN_ID);

        // then:
        assertThat(extensionId).isEqualTo(EXTENSION_ID);
    }

    @Test
    public void shouldIncreaseLoanInterestWhenExtendingLoan() throws Exception {
        // given:
        BigDecimal interest = new BigDecimal("12.13");
        Loan loan = simpleLoan();
        loan.setInterest(interest);
        loan.setId(LOAN_ID);
        when(loanService.findLoanById(LOAN_ID)).thenReturn(loan);

        Extension extension = simpleExtension();
        extension.setId(EXTENSION_ID);
        when(extensionRepository.save(any(Extension.class))).thenReturn(extension);

        // when:
        extensionService.extendLoan(LOAN_ID);

        // then:
        ArgumentCaptor<Loan> loanCapture = ArgumentCaptor.forClass(Loan.class);
        verify(loanService).save(loanCapture.capture());
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
        when(loanService.findLoanById(LOAN_ID)).thenReturn(loan);

        Extension extension = simpleExtension();
        extension.setId(EXTENSION_ID);
        when(extensionRepository.save(any(Extension.class))).thenReturn(extension);

        // when:
        extensionService.extendLoan(LOAN_ID);

        // then:
        ArgumentCaptor<Loan> loanCapture = ArgumentCaptor.forClass(Loan.class);
        verify(loanService).save(loanCapture.capture());
        LocalDateTime newDeadline = loanCapture.getValue().getEnd();
        assertThat(newDeadline.isAfter(end)).isTrue();
    }
}
