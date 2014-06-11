package com.stefanski.loan.core.repository;

import com.stefanski.loan.core.domain.Customer;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static com.stefanski.loan.util.TestDataFixture.simpleCustomer;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Dariusz Stefanski
 */
public class CustomerRepositoryTest extends RepositoryIntegrationTest {

    @Autowired
    private CustomerRepository customerRepository;

    @Test
    public void shouldFindCustomerById() throws Exception {
        // given:
        Customer customer = simpleCustomer();
        customer = customerRepository.save(customer);
        long customerId = customer.getId();

        // when:
        Customer foundCustomer = customerRepository.findOne(customerId);

        // then:
        assertThat(foundCustomer).isNotNull();
    }
}