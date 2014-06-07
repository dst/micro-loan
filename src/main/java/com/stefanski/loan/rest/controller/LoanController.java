package com.stefanski.loan.rest.controller;

import com.stefanski.loan.core.domain.Loan;
import com.stefanski.loan.core.repository.LoanRepository;
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
@RequestMapping(value = "/loans")
public class LoanController {

    @Autowired
    private LoanRepository loanRepository;

    @RequestMapping(method = POST)
    //TODO(dst): change to sth like: /customers/1/loans
    public ResponseEntity<Long> createLoan(@RequestBody Loan loan, UriComponentsBuilder builder) {
        Loan createdLoan = loanRepository.save(loan);
        log.info("Created loan: {}", loan);
        Long loanId = createdLoan.getId();

        HttpHeaders headers = new HttpHeaders();
        URI loanUri = builder.path("/loans/{id}").buildAndExpand(loanId).toUri();
        headers.setLocation(loanUri);

        return new ResponseEntity<Long>(loanId, headers, CREATED);

    }

    @RequestMapping(value = "/{loanId}", method = GET)
    public ResponseEntity<Loan> viewLoan(@PathVariable Long loanId) {
        Loan loan = loanRepository.findOne(loanId);
        if (loan == null) {
            return new ResponseEntity<Loan>(NOT_FOUND);
        } else {
            log.info("Found loan: {}", loan);
            return new ResponseEntity<Loan>(loan, OK);
        }
    }
}
