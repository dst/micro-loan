package com.stefanski.loan.rest.controller;

import com.stefanski.loan.core.domain.Loan;
import com.stefanski.loan.core.ex.RiskTooHighException;
import com.stefanski.loan.core.service.LoanService;
import com.stefanski.loan.rest.model.request.LoanReq;
import com.stefanski.loan.rest.model.response.CreationResp;
import com.stefanski.loan.rest.model.response.ErrorResp;
import com.stefanski.loan.rest.model.response.LoanResp;
import com.wordnik.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

import static java.net.HttpURLConnection.HTTP_BAD_REQUEST;
import static java.net.HttpURLConnection.HTTP_CREATED;
import static java.net.HttpURLConnection.HTTP_FORBIDDEN;
import static java.net.HttpURLConnection.HTTP_NOT_FOUND;
import static java.net.HttpURLConnection.HTTP_OK;
import static java.util.stream.Collectors.toList;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;


/**
 * @author Dariusz Stefanski
 */
@Slf4j
@RequestMapping("/loans")
@RestController
@Api(value = "Loans", description = "Loans management")
public class LoanController extends AbstractRestController {

    private LoanService loanService;

    @Autowired
    public LoanController(LoanService loanService) {
        this.loanService = loanService;
    }

    @RequestMapping(value = "", method = POST)
    @ApiOperation(value = "Creates a new loan for a given customer", notes = "Returns ID of created loan",
            response = CreationResp.class)
    @ApiResponses(value = {
            @ApiResponse(code = HTTP_CREATED, message = "Success"),
            @ApiResponse(code = HTTP_BAD_REQUEST, message = "Invalid input", response = ErrorResp.class),
            @ApiResponse(code = HTTP_FORBIDDEN, message = "Risk of giving loan is too high", response = ErrorResp.class)
    })
    public ResponseEntity<CreationResp> createLoan(
            @ApiParam(value = "Loan object that needs to be created")
            @Valid @RequestBody LoanReq loanReq,
            HttpServletRequest req) throws RiskTooHighException {

        loanReq.setIp(req.getRemoteAddr());
        Long loanId = loanService.applyForLoan(loanReq);
        HttpHeaders headers = getHttpHeadersWithLocation("/{loanId}", loanId);
        CreationResp creation = new CreationResp(loanId);
        return new ResponseEntity<>(creation, headers, CREATED);
    }

    @RequestMapping(value = "/{loanId}", method = GET)
    @ApiOperation(value = "Finds loan by ID", notes = "Returns a loan based on ID",
            response = LoanResp.class)
    @ApiResponses(value = {
            @ApiResponse(code = HTTP_OK, message = "Success"),
            @ApiResponse(code = HTTP_BAD_REQUEST, message = "Invalid input", response = ErrorResp.class),
            @ApiResponse(code = HTTP_NOT_FOUND, message = "Resource not found", response = ErrorResp.class)
    })
    public ResponseEntity<LoanResp> findLoan(
            @ApiParam(value = "ID of loan that needs to be fetched")
            @PathVariable Long loanId) {

        Loan loan = loanService.findLoanById(loanId);
        LoanResp resp = LoanResp.fromLoan(loan);
        return new ResponseEntity<>(resp, OK);
    }

    @RequestMapping(value = "", method = GET)
    @ApiOperation(value = "Finds all loans belonging to the given customer", notes = "Returns all loans based on owner's ID",
            response = LoanResp.class, responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = HTTP_OK, message = "Success"),
            @ApiResponse(code = HTTP_BAD_REQUEST, message = "Invalid input", response = ErrorResp.class),
            @ApiResponse(code = HTTP_NOT_FOUND, message = "Resource not found", response = ErrorResp.class)
    })
    public ResponseEntity<List<LoanResp>> findCustomerLoans(
            @ApiParam(value = "ID of customer that all his loans needs to be fetched")
            @RequestParam Long customerId) {

        List<Loan> loans = loanService.findCustomerLoans(customerId);
        List<LoanResp> resp = loans.stream().map(LoanResp::fromLoan).collect(toList());
        return new ResponseEntity<>(resp, OK);
    }
}
