package com.stefanski.loan.core.repository;

import com.stefanski.loan.Application;
import com.stefanski.loan.core.domain.Customer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = Application.class)
public class CustomerRepositoryIntegrationTest {

    @Autowired
    private CustomerRepository customerRepository;

    @Test
    public void shouldFindCustomerById() throws Exception {
        // given:
        Customer customer = new Customer();
        customer = customerRepository.save(customer);
        long customerId = customer.getId();

        // when:
        Customer foundCustomer = customerRepository.findOne(customerId);

        // then:
        assertThat(foundCustomer).isNotNull();
    }
}