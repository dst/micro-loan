package com.stefanski.loan.rest.model.response;

import com.stefanski.loan.core.domain.Loan;
import com.stefanski.loan.rest.util.Formatter;
import lombok.Data;

import javax.persistence.Entity;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Dariusz Stefanski
 */
@Data
public class LoanResp {

    private Long id;
    private BigDecimal amount;
    private BigDecimal interest;
    private String start;
    private String end;
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
