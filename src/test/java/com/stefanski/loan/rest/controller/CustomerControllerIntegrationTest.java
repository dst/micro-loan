package com.stefanski.loan.rest.controller;

import com.stefanski.loan.core.domain.Customer;
import com.stefanski.loan.core.repository.CustomerRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static com.stefanski.loan.util.TestDataFixture.CUSTOMER_ID;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class CustomerControllerIntegrationTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerController controller;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    public void shouldViewCustomerUseHttpNotFound() throws Exception {
        when(customerRepository.findOne(CUSTOMER_ID)).thenReturn(null);

        mockMvc.perform(get("/customers/{id}", CUSTOMER_ID))
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldViewCustomerUseHttpOk() throws Exception {
        when(customerRepository.findOne(CUSTOMER_ID)).thenReturn(new Customer());

        mockMvc.perform(get("/customers/{id}", CUSTOMER_ID))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldViewCustomerRenderCorrectly() throws Exception {
        Customer customer = new Customer();
        customer.setFirstName("Name");
        customer.setLastName("Surname");
        when(customerRepository.findOne(any(Long.class))).thenReturn(customer);

        mockMvc.perform(get("/customers/{id}", CUSTOMER_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Name"))
                .andExpect(jsonPath("$.lastName").value("Surname"));
    }
}