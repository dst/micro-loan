package com.stefanski.loan.rest.controller;

import com.stefanski.loan.core.domain.Customer;
import com.stefanski.loan.core.repository.CustomerRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

import static org.springframework.http.HttpStatus.*;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * @author Dariusz Stefanski
 */
@Slf4j
@Controller
@RequestMapping("/customers")
public class CustomerController {

    @Autowired
    private CustomerRepository customerRepository;

    @RequestMapping(method = POST)
    public ResponseEntity<Long> createCustomer(@RequestBody Customer customer, UriComponentsBuilder builder) {
        Customer createdCustomer = customerRepository.save(customer);
        log.info("Created customer: {}", customer);
        Long customerId = createdCustomer.getId();

        HttpHeaders headers = new HttpHeaders();
        URI customerUri = builder.path("/customers/{id}").buildAndExpand(customerId).toUri();
        headers.setLocation(customerUri);

        return new ResponseEntity<Long>(customerId, headers, CREATED);
    }

    @RequestMapping(value = "/{customerId}", method = GET)
    public ResponseEntity<Customer> viewCustomer(@PathVariable Long customerId) {
        Customer customer = customerRepository.findOne(customerId);
        if (customer == null) {
            return new ResponseEntity<Customer>(NOT_FOUND);
        } else {
            log.info("Found customer: {}", customer);
            return new ResponseEntity<Customer>(customer, OK);
        }
    }
}
