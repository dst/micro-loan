package com.stefanski.loan.rest.controller;

import com.stefanski.loan.core.domain.Customer;
import com.stefanski.loan.core.service.CustomerService;
import com.stefanski.loan.rest.model.request.CustomerReq;
import com.stefanski.loan.rest.model.response.CreationResp;
import com.stefanski.loan.rest.model.response.CustomerResp;
import com.stefanski.loan.rest.model.response.ErrorResp;
import com.wordnik.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static java.net.HttpURLConnection.*;
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
@Api(value = "Customers", description = "Customers management")
public class CustomerController extends AbstractRestController {

    @Autowired
    private CustomerService customerService;

    @RequestMapping(method = POST)
    @ApiOperation(value = "Creates a new customer", notes = "Returns ID of created customer",
            response = CreationResp.class)
    @ApiResponses(value = {
            @ApiResponse(code = HTTP_CREATED, message = "Success"),
            @ApiResponse(code = HTTP_BAD_REQUEST, message = "Invalid input", response = ErrorResp.class)
    })
    public ResponseEntity<CreationResp> createCustomer(
            @ApiParam(value = "Customer object that needs to be created")
            @Valid @RequestBody CustomerReq customerReq) {

        Long customerId = customerService.create(customerReq);
        HttpHeaders headers = getHttpHeadersWithLocation("/{customerId}", customerId);
        CreationResp creation = new CreationResp(customerId);
        return new ResponseEntity<>(creation, headers, CREATED);
    }

    @RequestMapping(value = "/{customerId}", method = GET)
    @ApiOperation(value = "Finds customer by ID", notes = "Returns a customer based on ID",
            response = CustomerResp.class)
    @ApiResponses(value = {
            @ApiResponse(code = HTTP_OK, message = "Success"),
            @ApiResponse(code = HTTP_BAD_REQUEST, message = "Invalid input", response = ErrorResp.class),
            @ApiResponse(code = HTTP_NOT_FOUND, message = "Resource not found", response = ErrorResp.class)
    })
    public ResponseEntity<CustomerResp> findCustomer(
            @ApiParam(value = "ID of customer that needs to be fetched")
            @PathVariable Long customerId) {

        Customer customer = customerService.findById(customerId);
        CustomerResp resp = CustomerResp.fromCustomer(customer);
        return new ResponseEntity<>(resp, OK);
    }
}
