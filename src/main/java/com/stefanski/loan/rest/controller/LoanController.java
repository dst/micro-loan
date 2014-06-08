package com.stefanski.loan.rest.controller;

import com.stefanski.loan.core.domain.Loan;
import com.stefanski.loan.core.error.ResourceNotFoundException;
import com.stefanski.loan.core.service.LoanService;
import com.stefanski.loan.rest.model.request.LoanRequest;
import com.stefanski.loan.rest.model.response.CreationResp;
import com.stefanski.loan.rest.model.response.LoanResp;
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
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;


/**
 * @author Dariusz Stefanski
 */
@Slf4j
@Controller
public class LoanController {

    @Autowired
    private LoanService loanService;

    @RequestMapping(value = "/customers/{customerId}/loans", method = POST)
    public ResponseEntity<CreationResp> createLoan(
            @PathVariable Long customerId,
            @Valid @RequestBody LoanRequest loanReq,
            UriComponentsBuilder builder) throws ResourceNotFoundException {

        Long loanId = loanService.applyForLoan(customerId, loanReq);
        HttpHeaders headers = getHttpHeadersForNewLoan(loanId, customerId, builder);
        CreationResp creation = new CreationResp(loanId);
        return new ResponseEntity<>(creation, headers, CREATED);
    }

    @RequestMapping(value = "/customers/{customerId}/loans/{loanId}", method = GET)
    public ResponseEntity<LoanResp> viewLoan(@PathVariable Long customerId, @PathVariable Long loanId)
            throws ResourceNotFoundException {

        Loan loan = loanService.findById(loanId);
        LoanResp loanResp = LoanResp.fromLoan(loan);
        return new ResponseEntity<>(loanResp, OK);
    }


    @RequestMapping(value = "/customers/{customerId}/loans", method = GET)
    public ResponseEntity<List<LoanResp>> viewLoans(@PathVariable Long customerId)
            throws ResourceNotFoundException {

        List<Loan> loans = loanService.findCustomerLoans(customerId);
        List<LoanResp> loanResps = loans.stream().map(LoanResp::fromLoan).collect(toList());
        return new ResponseEntity<>(loanResps, OK);
    }


    private HttpHeaders getHttpHeadersForNewLoan(Long loanId, Long customerId, UriComponentsBuilder builder) {
        HttpHeaders headers = new HttpHeaders();
        URI loanUri = builder.path("/customers/{customerId}/loans/{loanId}")
                .buildAndExpand(customerId, loanId).toUri();
        headers.setLocation(loanUri);
        return headers;
    }
}
