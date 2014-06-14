package com.stefanski.loan.rest.controller;

import com.stefanski.loan.core.domain.Loan;
import com.stefanski.loan.core.ex.ResourceNotFoundException;
import com.stefanski.loan.core.ex.RiskTooHighException;
import com.stefanski.loan.core.service.LoanService;
import com.stefanski.loan.rest.model.request.LoanRequest;
import com.stefanski.loan.rest.model.response.CreationResp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;


/**
 * @author Dariusz Stefanski
 */
@Slf4j
public class LoanController extends AbstractRestController {

    @Autowired
    private LoanService loanService;

    @RequestMapping(value = "/customers/{customerId}/loans", method = POST)
    public ResponseEntity<CreationResp> createLoan(
            @PathVariable Long customerId, @Valid @RequestBody LoanRequest loanReq,
            HttpServletRequest req)
            throws ResourceNotFoundException, RiskTooHighException {

        loanReq.setIp(req.getRemoteAddr());
        Long loanId = loanService.applyForLoan(customerId, loanReq);
        HttpHeaders headers = getHttpHeadersWithLocation("/{loanId}", loanId);
        CreationResp creation = new CreationResp(loanId);
        return new ResponseEntity<>(creation, headers, CREATED);
    }

    @RequestMapping(value = "/customers/{customerId}/loans/{loanId}/extensions", method = POST)
    public ResponseEntity<CreationResp> createExtension(
            @PathVariable Long customerId, @PathVariable Long loanId)
            throws ResourceNotFoundException {

        Long extensionId = loanService.extendLoan(loanId);
        HttpHeaders headers = getHttpHeadersWithLocation("/{extensionId}", extensionId);
        CreationResp creation = new CreationResp(extensionId);
        return new ResponseEntity<>(creation, headers, CREATED);
    }

    @RequestMapping(value = "/customers/{customerId}/loans/{loanId}", method = GET)
    public ResponseEntity<Loan> findLoan(@PathVariable Long customerId, @PathVariable Long loanId)
            throws ResourceNotFoundException {

        Loan loan = loanService.findById(loanId);
        return new ResponseEntity<>(loan, OK);
    }

    @RequestMapping(value = "/customers/{customerId}/loans", method = GET)
    public ResponseEntity<List<Loan>> findCustomerLoans(@PathVariable Long customerId)
            throws ResourceNotFoundException {

        List<Loan> loans = loanService.findCustomerLoans(customerId);
        return new ResponseEntity<>(loans, OK);
    }
}
