package com.stefanski.loan.core.service;

import com.stefanski.loan.core.domain.Customer;
import com.stefanski.loan.core.ex.ResourceNotFoundException;
import com.stefanski.loan.core.repository.CustomerRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static com.stefanski.loan.util.TestDataFixture.CUSTOMER_ID;
import static com.stefanski.loan.util.TestDataFixture.simpleCustomerReq;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

/**
 * @author Dariusz Stefanski
 */
public class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerService customerService;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void shouldFindCustomerById() throws Exception {
        // given:
        Customer customer = new Customer();
        customer.setId(CUSTOMER_ID);
        when(customerRepository.findOne(CUSTOMER_ID)).thenReturn(customer);

        // when:
        Customer foundCustomer = customerService.findById(CUSTOMER_ID);

        // then:
        assertThat(foundCustomer).isNotNull();
        assertThat(foundCustomer.getId()).isEqualTo(CUSTOMER_ID);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void shouldThrowExceptionWhenSearchingNonExistingCustomer() throws Exception {
        // given:
        when(customerRepository.findOne(CUSTOMER_ID)).thenReturn(null);

        // when:
        customerService.findById(CUSTOMER_ID);
    }


    @Test
    public void shouldReturnIdOfCreatedCustomer() throws Exception {
        // given:
        Customer customer = new Customer();
        customer.setId(CUSTOMER_ID);
        when(customerRepository.save(any(Customer.class))).thenReturn(customer);

        // when:
        Long customerId = customerService.create(simpleCustomerReq());

        // then:
        assertThat(customerId).isEqualTo(CUSTOMER_ID);
    }
}