package com.stefanski.loan.rest.controller;

import com.stefanski.loan.core.domain.Loan;
import com.stefanski.loan.core.repository.LoanRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;

import static com.stefanski.loan.util.TestDataFixture.CUSTOMER_ID;
import static com.stefanski.loan.util.TestDataFixture.LOAN_ID;
import static com.stefanski.loan.util.TestHelper.APPLICATION_JSON_UTF8;
import static org.hamcrest.core.Is.is;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * @author Dariusz Stefanski
 */
public class LoanControllerIntegrationTest extends ControllerIntegrationTest {

    @Mock
    private LoanRepository LoanRepository;

    @InjectMocks
    private LoanController controller;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        super.setup(controller);
    }

    @Test
    public void shouldViewLoanUseHttpOk() throws Exception {
        when(LoanRepository.findOne(LOAN_ID)).thenReturn(new Loan());

        mockMvc.perform(get("/customers/{customerId}/loans/{loanId}", CUSTOMER_ID, LOAN_ID))
                .andExpect(status().isOk());
    }


    @Test
    public void shouldViewLoanUseHttpNotFound() throws Exception {
        when(LoanRepository.findOne(LOAN_ID)).thenReturn(null);

        mockMvc.perform(get("/customers/{customerId}/loans/{loanId}", CUSTOMER_ID, LOAN_ID))
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldViewLoanRenderCorrectly() throws Exception {
        Loan loan = new Loan();
        loan.setAmount(BigDecimal.TEN);
        when(LoanRepository.findOne(any(Long.class))).thenReturn(loan);

        mockMvc.perform(get("/customers/{id}/loans/{id}", CUSTOMER_ID, LOAN_ID))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.amount", is(10)));
    }

}
