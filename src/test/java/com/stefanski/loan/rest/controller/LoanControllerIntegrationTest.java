package com.stefanski.loan.rest.controller;

import com.stefanski.loan.core.domain.Loan;
import com.stefanski.loan.core.ex.ResourceNotFoundException;
import com.stefanski.loan.core.service.LoanService;
import com.stefanski.loan.rest.model.request.LoanRequest;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;

import static com.stefanski.loan.util.TestDataFixture.*;
import static com.stefanski.loan.util.TestHelper.APPLICATION_JSON_UTF8;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * @author Dariusz Stefanski
 */
public class LoanControllerIntegrationTest extends ControllerIntegrationTest {

    private static final String CUSTOMER_LOANS_URL = String.format("/customers/%s/loans", CUSTOMER_ID);

    @Mock
    private LoanService loanService;

    @InjectMocks
    private LoanController controller;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        super.setup(controller);
    }

    @Test
    public void shouldReturnBadRequestIfAmountIsMissing() throws Exception {
        LoanRequest loanReq = new LoanRequest();
        loanReq.setDaysCount(30);

        mockMvc.perform(postWithJson(CUSTOMER_LOANS_URL, loanReq))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is("Invalid parameters")))
                .andExpect(jsonPath("$.details[0].field", is("amount")))
                .andExpect(jsonPath("$.details[0].message", is("may not be null")));
    }

    @Test
    public void shouldReturnIdForCreatedLoan() throws Exception {
        LoanRequest loanReq = simpleLoanReqest();
        when(loanService.applyForLoan(CUSTOMER_ID, loanReq)).thenReturn(LOAN_ID);

        mockMvc.perform(postWithJson(CUSTOMER_LOANS_URL, loanReq))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id", is(LOAN_ID.intValue())));
    }

    @Test
    public void shouldCreateLoanLocation() throws Exception {
        LoanRequest loanReq = simpleLoanReqest();
        when(loanService.applyForLoan(CUSTOMER_ID, loanReq)).thenReturn(LOAN_ID);

        //TODO(dst): get rid off http://localhost
        String expectedLocation = "http://localhost" + CUSTOMER_LOANS_URL + "/" + LOAN_ID;

        mockMvc.perform(postWithJson(CUSTOMER_LOANS_URL, loanReq))
                .andExpect(header().string("Location", is(expectedLocation)));
    }

    @Test
    public void shouldViewLoanUseHttpOk() throws Exception {
        when(loanService.findById(LOAN_ID)).thenReturn(new Loan());

        mockMvc.perform(get(CUSTOMER_LOANS_URL + "/" + LOAN_ID))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldViewLoanUseHttpNotFound() throws Exception {
        when(loanService.findById(LOAN_ID)).thenThrow(new ResourceNotFoundException());

        mockMvc.perform(get(CUSTOMER_LOANS_URL + "/" + LOAN_ID))
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldViewLoanRenderCorrectly() throws Exception {
        Loan loan = simpleLoanWithAmount(BigDecimal.TEN);
        when(loanService.findById(LOAN_ID)).thenReturn(loan);

        mockMvc.perform(get(CUSTOMER_LOANS_URL + "/" + LOAN_ID))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.amount", is(10)));
    }
}
