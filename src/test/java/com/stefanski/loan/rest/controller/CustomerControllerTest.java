package com.stefanski.loan.rest.controller;

import com.stefanski.loan.core.domain.Customer;
import com.stefanski.loan.core.ex.ResourceNotFoundException;
import com.stefanski.loan.core.service.CustomerService;
import com.stefanski.loan.rest.error.ErrorMessage;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static com.stefanski.loan.rest.error.ErrorMessage.INVALID_PARAMETERS_MSG;
import static com.stefanski.loan.util.TestDataFixture.*;
import static com.stefanski.loan.util.TestHelper.APPLICATION_JSON_UTF8;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.when;
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
        Customer customer = new Customer();
        customer.setFirstName("John");

        mockMvc.perform(postWithJson("/customers", customer))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.message", is(INVALID_PARAMETERS_MSG)))
                .andExpect(jsonPath("$.details[0].field", is("lastName")))
                .andExpect(jsonPath("$.details[0].message", is("may not be empty")));
    }

    @Test
    public void shouldReturnIdForCreatedCustomer() throws Exception {
        Customer customer = simpleCustomer();
        when(customerService.create(customer)).thenReturn(CUSTOMER_ID);

        mockMvc.perform(postWithJson("/customers", customer))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id", is(CUSTOMER_ID.intValue())));
    }


    @Test
    public void shouldCreateCustomerWithPolishLetters() throws Exception {
        Customer customer = polishCustomer();

        when(customerService.create(customer)).thenReturn(CUSTOMER_ID);

        mockMvc.perform(postWithJson("/customers", customer))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id", is(CUSTOMER_ID.intValue())));
    }

    @Test
    public void shouldCreateCustomerLocation() throws Exception {
        Customer customer = simpleCustomer();

        when(customerService.create(customer)).thenReturn(CUSTOMER_ID);

        mockMvc.perform(postWithJson("/customers", customer))
                .andExpect(header().string("Location",
                        Matchers.endsWith("/customers/" + CUSTOMER_ID)));
    }

    @Test
    public void shouldReturnHttpOkWhenSearchingExistingCustomer() throws Exception {
        when(customerService.findById(CUSTOMER_ID)).thenReturn(new Customer());

        mockMvc.perform(get("/customers/{id}", CUSTOMER_ID))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldReturnHttpNotFoundWhenSearchingNotExistingCustomer() throws Exception {
        when(customerService.findById(CUSTOMER_ID)).thenThrow(new ResourceNotFoundException());

        mockMvc.perform(get("/customers/{id}", CUSTOMER_ID))
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldCustomerRenderCorrectly() throws Exception {
        Customer customer = simpleCustomer();
        when(customerService.findById(CUSTOMER_ID)).thenReturn(customer);

        mockMvc.perform(get("/customers/{id}", CUSTOMER_ID))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.firstName", is(SIMPLE_FIRST_NAME)))
                .andExpect(jsonPath("$.lastName", is(SIMPLE_LAST_NAME)));
    }
}