package com.stefanski.loan.core.service;

import com.stefanski.loan.core.domain.Customer;
import com.stefanski.loan.core.ex.ResourceNotFoundException;
import com.stefanski.loan.core.repository.CustomerRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Dariusz Stefanski
 */
@Slf4j
@Transactional
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

        log.debug("Found customer: {}", customer);
        return customer;
    }

    public Long create(Customer customer) {
        Customer createdCustomer = customerRepository.save(customer);
        log.info("Created customer: {}", createdCustomer);
        return createdCustomer.getId();
    }
}
