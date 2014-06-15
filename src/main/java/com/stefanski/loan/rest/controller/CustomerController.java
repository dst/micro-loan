package com.stefanski.loan.rest.controller;

import com.stefanski.loan.core.domain.Customer;
import com.stefanski.loan.core.ex.ResourceNotFoundException;
import com.stefanski.loan.core.service.CustomerService;
import com.stefanski.loan.rest.model.response.CreationResp;
import com.stefanski.loan.rest.model.response.CustomerResp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
@RestController
public class CustomerController extends AbstractRestController {

    @Autowired
    private CustomerService customerService;

    /**
     * Creates customer.
     *
     * @param customer customer data
     * @return id of created customer
     */
    @RequestMapping(method = POST)
    public ResponseEntity<CreationResp> createCustomer(@Valid @RequestBody Customer customer) {
        Long customerId = customerService.create(customer);
        HttpHeaders headers = getHttpHeadersWithLocation("/{customerId}", customerId);
        CreationResp creation = new CreationResp(customerId);
        return new ResponseEntity<>(creation, headers, CREATED);
    }

    /**
     * Finds customer with given id.
     *
     * @param customerId searched id
     * @return found customer
     * @throws ResourceNotFoundException if customer was not found
     */
    @RequestMapping(value = "/{customerId}", method = GET)
    public ResponseEntity<CustomerResp> findCustomer(@PathVariable Long customerId)
            throws ResourceNotFoundException {

        Customer customer = customerService.findById(customerId);
        CustomerResp resp = CustomerResp.fromCustomer(customer);
        return new ResponseEntity<>(resp, OK);
    }
}
