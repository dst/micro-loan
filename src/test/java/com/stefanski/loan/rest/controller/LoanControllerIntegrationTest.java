package com.stefanski.loan.rest.controller;

import com.stefanski.loan.core.domain.Loan;
import com.stefanski.loan.core.repository.LoanRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;

import static com.stefanski.loan.util.TestDataFixture.LOAN_ID;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class LoanControllerIntegrationTest {

    @Mock
    private LoanRepository LoanRepository;

    @InjectMocks
    private LoanController controller;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    public void shouldViewLoanUseHttpNotFound() throws Exception {
        when(LoanRepository.findOne(LOAN_ID)).thenReturn(null);

        mockMvc.perform(get("/loans/{id}", LOAN_ID))
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldViewLoanUseHttpOk() throws Exception {
        when(LoanRepository.findOne(LOAN_ID)).thenReturn(new Loan());

        mockMvc.perform(get("/loans/{id}", LOAN_ID))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldViewLoanRenderCorrectly() throws Exception {
        Loan loan = new Loan();
        loan.setAmount(BigDecimal.TEN);
        when(LoanRepository.findOne(any(Long.class))).thenReturn(loan);

        mockMvc.perform(get("/loans/{id}", LOAN_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.amount").value(10));
    }

}
