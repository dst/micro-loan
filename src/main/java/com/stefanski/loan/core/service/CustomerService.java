package com.stefanski.loan.core.service;

import com.stefanski.loan.core.domain.Customer;
import com.stefanski.loan.core.error.ResourceNotFoundException;
import com.stefanski.loan.core.repository.CustomerRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Dariusz Stefanski
 */
@Slf4j
@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    public Customer findById(Long customerId) throws ResourceNotFoundException {
        Customer customer = customerRepository.findOne(customerId);
        if (customer == null) {
            String msg = String.format("Customer with id %d does not exist", customerId);
            throw new ResourceNotFoundException(msg);
        }
        return customer;
    }

    public Long create(Customer customer) {
        Customer createdCustomer = customerRepository.save(customer);
        log.debug("Created customer: {}", createdCustomer);
        Long customerId = createdCustomer.getId();
        return customerId;
    }
}
