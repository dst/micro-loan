package com.stefanski.loan.rest.model.response;

import com.stefanski.loan.core.domain.Loan;
import com.stefanski.loan.rest.util.Formatter;
import com.wordnik.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Dariusz Stefanski
 */
@Data
public class LoanResp {

    @ApiModelProperty(value = "unique identifier for the loan")
    private Long id;

    @ApiModelProperty(value = "amount of money which was borrow", required = true)
    private BigDecimal amount;

    @ApiModelProperty(value="annual interest", required = true)
    private BigDecimal interest;

    @ApiModelProperty(value = "starting day in ISO_DATE format (yyyy-mm-dd)", required = true)
    private String start;

    @ApiModelProperty(value = "ending day in ISO_DATE format (yyyy-mm-dd)", required = true)
    private String end;

    @ApiModelProperty(value="all extensions related to this loan", required = true)
    private List<ExtensionResp> extensions;

    public static LoanResp fromLoan(Loan loan) {
        LoanResp resp = new LoanResp();
        resp.setId(loan.getId());
        resp.setAmount(loan.getAmount());
        resp.setInterest(loan.getInterest());
        resp.setStart(Formatter.formatTime(loan.getStart()));
        resp.setEnd(Formatter.formatTime(loan.getEnd()));

        List<ExtensionResp> exts = loan.getExtensions()
                .stream()
                .map(ExtensionResp::fromExtension)
                .collect(Collectors.toList());
        resp.setExtensions(exts);

        return resp;
    }
}
