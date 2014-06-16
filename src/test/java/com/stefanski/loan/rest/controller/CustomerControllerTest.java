package com.stefanski.loan.rest.controller;

import com.stefanski.loan.core.domain.Customer;
import com.stefanski.loan.core.ex.ResourceNotFoundException;
import com.stefanski.loan.core.service.CustomerService;
import com.stefanski.loan.rest.model.request.CustomerReq;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static com.stefanski.loan.rest.model.response.ErrorResp.INVALID_PARAM_ERR;
import static com.stefanski.loan.util.TestDataFixture.*;
import static com.stefanski.loan.util.TestHelper.APPLICATION_JSON_UTF8;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * @author Dariusz Stefanski
 */
public class CustomerControllerTest extends ControllerIntegrationTest {

    @Mock
    private CustomerService customerService;

    @InjectMocks
    private CustomerController controller;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        super.setup(controller);
    }

    @Test
    public void shouldReturnBadRequestIfLastNameIsMissing() throws Exception {
        CustomerReq customerReq = new CustomerReq();
        customerReq.setFirstName("John");

        mockMvc.perform(postWithJson("/customers", customerReq))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldReturnErrorRespIfLastNameIsMissing() throws Exception {
        CustomerReq customerReq = new CustomerReq();
        customerReq.setFirstName("John");

        mockMvc.perform(postWithJson("/customers", customerReq))
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.error", is(INVALID_PARAM_ERR)))
                .andExpect(jsonPath("$.message", is("lastName may not be empty")))
                .andExpect(jsonPath("$.status", is(BAD_REQUEST.value())))
                .andExpect(jsonPath("$.timestamp", greaterThan(0L)));
    }


    @Test
    public void shouldReturnIdOfCreatedCustomer() throws Exception {
        CustomerReq customerReq = simpleCustomerReq();
        when(customerService.create(customerReq)).thenReturn(CUSTOMER_ID);

        mockMvc.perform(postWithJson("/customers", customerReq))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id", is(CUSTOMER_ID.intValue())));
    }

    @Test
    public void shouldCreateCustomerWithPolishLetters() throws Exception {
        CustomerReq customer = polishCustomerReq();

        when(customerService.create(customer)).thenReturn(CUSTOMER_ID);

        mockMvc.perform(postWithJson("/customers", customer))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id", is(CUSTOMER_ID.intValue())));
    }

    @Test
    public void shouldCreateCustomerLocation() throws Exception {
        CustomerReq customerReq = simpleCustomerReq();

        when(customerService.create(customerReq)).thenReturn(CUSTOMER_ID);

        mockMvc.perform(postWithJson("/customers", customerReq))
                .andExpect(header().string("Location",
                        Matchers.endsWith("/customers/" + CUSTOMER_ID)));
    }

    @Test
    public void shouldReturnHttpOkWhenSearchingExistingCustomer() throws Exception {
        when(customerService.findById(CUSTOMER_ID)).thenReturn(simpleCustomer());

        mockMvc.perform(get("/customers/{id}", CUSTOMER_ID))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldPathWithTrailingSlashBeAccepted() throws Exception {
        when(customerService.findById(CUSTOMER_ID)).thenReturn(simpleCustomer());

        mockMvc.perform(get("/customers/{id}/", CUSTOMER_ID))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldReturnHttpNotFoundWhenSearchingNotExistingCustomer() throws Exception {
        when(customerService.findById(CUSTOMER_ID)).thenThrow(new ResourceNotFoundException());

        mockMvc.perform(get("/customers/{id}", CUSTOMER_ID))
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldReturnCorrectCustomer() throws Exception {
        Customer customer = simpleCustomer();
        customer.setId(CUSTOMER_ID);
        when(customerService.findById(CUSTOMER_ID)).thenReturn(customer);

        mockMvc.perform(get("/customers/{id}", CUSTOMER_ID))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id", is(customer.getId().intValue())))
                .andExpect(jsonPath("$.firstName", is(customer.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(customer.getLastName())));
    }
}