package com.stefanski.loan.rest.controller;

import com.stefanski.loan.core.domain.Customer;
import com.stefanski.loan.core.ex.ResourceNotFoundException;
import com.stefanski.loan.core.service.CustomerService;
import com.stefanski.loan.rest.model.response.CreationResp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * @author Dariusz Stefanski
 */
@Slf4j
@RequestMapping("/customers")
public class CustomerController extends AbstractRestController {

    @Autowired
    private CustomerService customerService;

    @RequestMapping(method = POST)
    public ResponseEntity<CreationResp> createCustomer(@Valid @RequestBody Customer customer) {
        Long customerId = customerService.create(customer);
        HttpHeaders headers = getHttpHeadersWithLocation("/{customerId}", customerId);
        CreationResp creation = new CreationResp(customerId);
        return new ResponseEntity<>(creation, headers, CREATED);
    }

    @RequestMapping(value = "/{customerId}", method = GET)
    public ResponseEntity<Customer> findCustomer(@PathVariable Long customerId) throws ResourceNotFoundException {
        Customer customer = customerService.findById(customerId);
        return new ResponseEntity<>(customer, OK);
    }
}
