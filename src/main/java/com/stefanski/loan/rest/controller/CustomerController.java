package com.stefanski.loan.rest.controller;

import com.stefanski.loan.core.domain.Customer;
import com.stefanski.loan.core.error.ResourceNotFoundException;
import com.stefanski.loan.core.service.CustomerService;
import com.stefanski.loan.rest.model.Creation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
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
    private CustomerService customerService;

    @RequestMapping(method = POST)
    public ResponseEntity<Creation> createCustomer(@Valid @RequestBody Customer customer, UriComponentsBuilder builder) {
        Long customerId = customerService.create(customer);
        HttpHeaders headers = getHttpHeadersForNewCustomer(customerId, builder);
        Creation creation = new Creation(customerId);
        return new ResponseEntity<>(creation, headers, CREATED);
    }

    @RequestMapping(value = "/{customerId}", method = GET)
    public ResponseEntity<Customer> viewCustomer(@PathVariable Long customerId) throws ResourceNotFoundException {
        Customer customer = customerService.findById(customerId);
        return new ResponseEntity<>(customer, OK);
    }

    private HttpHeaders getHttpHeadersForNewCustomer(Long customerId, UriComponentsBuilder builder) {
        HttpHeaders headers = new HttpHeaders();
        URI customerUri = builder.path("/customers/{id}").buildAndExpand(customerId).toUri();
        headers.setLocation(customerUri);
        return headers;
    }
}
