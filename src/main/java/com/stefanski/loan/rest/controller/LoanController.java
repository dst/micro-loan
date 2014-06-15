package com.stefanski.loan.rest.controller;

import com.stefanski.loan.core.domain.Extension;
import com.stefanski.loan.core.domain.Loan;
import com.stefanski.loan.core.ex.ResourceNotFoundException;
import com.stefanski.loan.core.ex.RiskTooHighException;
import com.stefanski.loan.core.service.LoanService;
import com.stefanski.loan.rest.model.request.LoanRequest;
import com.stefanski.loan.rest.model.response.CreationResp;
import com.stefanski.loan.rest.model.response.ExtensionResp;
import com.stefanski.loan.rest.model.response.LoanResp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

import static java.util.stream.Collectors.toList;
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
public class LoanController extends AbstractRestController {

    @Autowired
    private LoanService loanService;

    /**
     * Creates a loan for a given customer if possible.
     *
     * @param customerId customer (id) who wants to take a loan
     * @param loanReq    loan details reported by customer
     * @param req        http request
     * @return id of created loan
     * @throws ResourceNotFoundException if customer was not found
     * @throws RiskTooHighException      if risk of giving loan to customer is too high
     */
    @RequestMapping(value = "/{customerId}/loans", method = POST)
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

    /**
     * Extends a loan.
     *
     * @param customerId customer (id) who wants to extend a loan
     * @param loanId     loan (id) which will be extended
     * @return id of extension
     * @throws ResourceNotFoundException if a loan was not found
     */
    @RequestMapping(value = "/{customerId}/loans/{loanId}/extensions", method = POST)
    public ResponseEntity<CreationResp> createExtension(
            @PathVariable Long customerId, @PathVariable Long loanId)
            throws ResourceNotFoundException {

        // customerId is not needed now, because loans have unique ids in system
        Long extensionId = loanService.extendLoan(loanId);
        HttpHeaders headers = getHttpHeadersWithLocation("/{extensionId}", extensionId);
        CreationResp creation = new CreationResp(extensionId);
        return new ResponseEntity<>(creation, headers, CREATED);
    }

    /**
     * Finds loan with given id.
     *
     * @param customerId owner (id) of searched loan
     * @param loanId     id of searched loan
     * @return found loan
     * @throws ResourceNotFoundException if loan was not found
     */
    @RequestMapping(value = "/{customerId}/loans/{loanId}", method = GET)
    public ResponseEntity<LoanResp> findLoan(@PathVariable Long customerId, @PathVariable Long loanId)
            throws ResourceNotFoundException {

        // customerId is not needed now, because loans have unique ids in system
        Loan loan = loanService.findLoanById(loanId);
        LoanResp resp = LoanResp.fromLoan(loan);
        return new ResponseEntity<>(resp, OK);
    }

    /**
     * Finds all loans belonging to given customer.
     *
     * @param customerId customer (id) who wants to find his/her loans
     * @return all loans
     * @throws ResourceNotFoundException if customer was not found
     */
    @RequestMapping(value = "/{customerId}/loans", method = GET)
    public ResponseEntity<List<LoanResp>> findCustomerLoans(@PathVariable Long customerId)
            throws ResourceNotFoundException {

        List<Loan> loans = loanService.findCustomerLoans(customerId);
        List<LoanResp> resp = loans.stream().map(LoanResp::fromLoan).collect(toList());
        return new ResponseEntity<>(resp, OK);
    }


    /**
     * Finds extension with given id.
     *
     * @param customerId  owner (id) of searched loan
     * @param loanId      loan (id) which has searched extension
     * @param extensionId id of searched extension
     * @return found extension
     * @throws ResourceNotFoundException if extension was not found
     */
    @RequestMapping(value = "/{customerId}/loans/{loanId}/extensions/{extensionId}", method = GET)
    public ResponseEntity<ExtensionResp> findExtension(
            @PathVariable Long customerId,
            @PathVariable Long loanId,
            @PathVariable Long extensionId)
            throws ResourceNotFoundException {

        // customerId and loanId are not needed now, because extensions have unique ids in system
        Extension ext = loanService.findExtensionById(extensionId);
        ExtensionResp resp = ExtensionResp.fromExtension(ext);
        return new ResponseEntity<>(resp, OK);
    }
}
