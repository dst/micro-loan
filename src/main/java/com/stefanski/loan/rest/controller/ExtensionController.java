package com.stefanski.loan.rest.controller;

import com.stefanski.loan.core.domain.Extension;
import com.stefanski.loan.core.service.ExtensionService;
import com.stefanski.loan.rest.model.response.CreationResp;
import com.stefanski.loan.rest.model.response.ErrorResp;
import com.stefanski.loan.rest.model.response.ExtensionResp;
import com.wordnik.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static java.net.HttpURLConnection.*;
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
@Api(value = "Extensions", description = "Extensions management")
public class ExtensionController extends AbstractRestController {

    @Autowired
    private ExtensionService extensionService;

    @RequestMapping(value = "/{loanId}/extensions", method = POST)
    @ApiOperation(value = "Extends a loan with given ID", notes = "Returns ID of extension",
            response = CreationResp.class)
    @ApiResponses(value = {
            @ApiResponse(code = HTTP_CREATED, message = "Success"),
            @ApiResponse(code = HTTP_BAD_REQUEST, message = "Invalid input", response = ErrorResp.class),
            @ApiResponse(code = HTTP_NOT_FOUND, message = "Resource not found", response = ErrorResp.class)
    })
    public ResponseEntity<CreationResp> createExtension(
            @ApiParam(value = "ID of loan that needs to be extended")
            @PathVariable Long loanId) {

        Long extensionId = extensionService.extendLoan(loanId);
        HttpHeaders headers = getHttpHeadersWithLocation("/{extensionId}", extensionId);
        CreationResp creation = new CreationResp(extensionId);
        return new ResponseEntity<>(creation, headers, CREATED);
    }

    @RequestMapping(value = "/{loanId}/extensions/{extensionId}", method = GET)
    @ApiOperation(value = "Finds loan extension by ID which is part of the given loan", notes = "Returns an extensions based on ID",
            response = ExtensionResp.class)
    @ApiResponses(value = {
            @ApiResponse(code = HTTP_OK, message = "Success"),
            @ApiResponse(code = HTTP_BAD_REQUEST, message = "Invalid input", response = ErrorResp.class),
            @ApiResponse(code = HTTP_NOT_FOUND, message = "Resource not found", response = ErrorResp.class)
    })
    public ResponseEntity<ExtensionResp> findExtension(
            @ApiParam(value = "ID of loan which contains searched extension")
            @PathVariable Long loanId,
            @ApiParam(value = "ID of extension that needs to be fetched")
            @PathVariable Long extensionId) {

        Extension ext = extensionService.findLoanExtension(loanId, extensionId);
        ExtensionResp resp = ExtensionResp.fromExtension(ext);
        return new ResponseEntity<>(resp, OK);
    }
}
