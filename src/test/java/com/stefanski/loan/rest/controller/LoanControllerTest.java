package com.stefanski.loan.rest.controller;

import com.stefanski.loan.core.domain.Extension;
import com.stefanski.loan.core.domain.Loan;
import com.stefanski.loan.core.ex.ResourceNotFoundException;
import com.stefanski.loan.core.risk.RiskTooHighException;
import com.stefanski.loan.core.service.LoanService;
import com.stefanski.loan.rest.model.request.LoanReq;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Arrays;

import static com.stefanski.loan.rest.model.response.ErrorResp.INVALID_PARAM_ERR;
import static com.stefanski.loan.rest.model.response.ErrorResp.RISK_TOO_HIGH_ERR;
import static com.stefanski.loan.util.TestDataFixture.*;
import static com.stefanski.loan.util.TestHelper.APPLICATION_JSON_UTF8;
import static java.time.format.DateTimeFormatter.ISO_DATE;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * @author Dariusz Stefanski
 */
public class LoanControllerTest extends ControllerIntegrationTest {

    private static final String LOANS_PREFIX = "/loans";

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
        LoanReq loanReq = new LoanReq();
        loanReq.setCustomerId(CUSTOMER_ID);
        loanReq.setDaysCount(30);

        mockMvc.perform(postWithJson(LOANS_PREFIX, loanReq))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldReturnErrorRespIfAmountIsMissing() throws Exception {
        LoanReq loanReq = new LoanReq();
        loanReq.setCustomerId(CUSTOMER_ID);
        loanReq.setDaysCount(30);

        mockMvc.perform(postWithJson(LOANS_PREFIX, loanReq))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.error", is(INVALID_PARAM_ERR)))
                .andExpect(jsonPath("$.message", is("amount may not be null")))
                .andExpect(jsonPath("$.status", is(BAD_REQUEST.value())))
                .andExpect(jsonPath("$.timestamp", greaterThan(0L)));
    }

    @Test
    public void shouldReturnForbiddenIfRiskIsTooHigh() throws Exception {
        LoanReq loanReq = simpleLoanReqest();

        String riskMsg = "Risk to high";
        when(loanService.applyForLoan(loanReq))
                .thenThrow(new RiskTooHighException(riskMsg));

        mockMvc.perform(postWithJson(LOANS_PREFIX, loanReq))
                .andExpect(status().isForbidden());
    }

    @Test
    public void shouldReturnErrorRespIfRiskIsTooHigh() throws Exception {
        LoanReq loanReq = simpleLoanReqest();

        String riskMsg = "Risk to high";
        when(loanService.applyForLoan(loanReq))
                .thenThrow(new RiskTooHighException(riskMsg));

        mockMvc.perform(postWithJson(LOANS_PREFIX, loanReq))
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.error", is(RISK_TOO_HIGH_ERR)))
                .andExpect(jsonPath("$.message", is(riskMsg)))
                .andExpect(jsonPath("$.status", is(FORBIDDEN.value())))
                .andExpect(jsonPath("$.timestamp", greaterThan(0L)));
    }


    @Test
    public void shouldReturnIdOfCreatedLoan() throws Exception {
        LoanReq loanReq = simpleLoanReqest();
        when(loanService.applyForLoan(loanReq)).thenReturn(LOAN_ID);

        mockMvc.perform(postWithJson(LOANS_PREFIX, loanReq))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id", is(LOAN_ID.intValue())));
    }

    @Test
    public void shouldCreateLocationToNewLoan() throws Exception {
        LoanReq loanReq = simpleLoanReqest();
        when(loanService.applyForLoan(loanReq)).thenReturn(LOAN_ID);

        mockMvc.perform(postWithJson(LOANS_PREFIX, loanReq))
                .andExpect(header().string("Location",
                        Matchers.endsWith(LOANS_PREFIX + "/" + LOAN_ID)));
    }

    @Test
    public void shouldFindLoanUseHttpOk() throws Exception {
        when(loanService.findLoanById(LOAN_ID)).thenReturn(simpleLoan());

        mockMvc.perform(get(LOANS_PREFIX + "/" + LOAN_ID))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldFindLoanUseHttpNotFound() throws Exception {
        when(loanService.findLoanById(LOAN_ID)).thenThrow(new ResourceNotFoundException());

        mockMvc.perform(get(LOANS_PREFIX + "/" + LOAN_ID))
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldReturnCorrectLoan() throws Exception {
        Loan loan = simpleLoan();
        loan.setId(LOAN_ID);

        when(loanService.findLoanById(LOAN_ID)).thenReturn(loan);

        mockMvc.perform(get(LOANS_PREFIX + "/" + LOAN_ID))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id", is(loan.getId().intValue())))
                .andExpect(jsonPath("$.amount", is(loan.getAmount().intValue())))
                .andExpect(jsonPath("$.interest", is(loan.getInterest().intValue())))
                .andExpect(jsonPath("$.start", is(loan.getStart().format(ISO_DATE))))
                .andExpect(jsonPath("$.end", is(loan.getEnd().format(ISO_DATE))));
    }

    @Test
    public void shouldReturnCorrectLoanExtension() throws Exception {
        Extension extension = new Extension();
        extension.setId(EXTENSION_ID);
        extension.setCreationTime(LocalDateTime.now());

        Loan loan = simpleLoan();
        loan.addExtension(extension);

        when(loanService.findLoanById(LOAN_ID)).thenReturn(loan);

        mockMvc.perform(get(LOANS_PREFIX + "/" + LOAN_ID))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.extensions", hasSize(1)))
                .andExpect(jsonPath("$.extensions[0].id", is(EXTENSION_ID.intValue())))
                .andExpect(jsonPath("$.extensions[0].creationTime", is(extension.getCreationTime().format(ISO_DATE))));
    }

    @Test
    public void shouldReturnCorrectCustomerLoans() throws Exception {
        Loan loan = simpleLoan();
        when(loanService.findCustomerLoans(CUSTOMER_ID))
                .thenReturn(Arrays.asList(loan, loan));

        mockMvc.perform(get(LOANS_PREFIX + "?customerId={customerId}", CUSTOMER_ID))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(2)));
    }
}
