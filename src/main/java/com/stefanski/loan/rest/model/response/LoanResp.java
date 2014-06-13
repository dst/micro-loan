package com.stefanski.loan.rest.model.response;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.stefanski.loan.core.domain.Extension;
import com.stefanski.loan.core.domain.Loan;
import com.stefanski.loan.rest.util.LocalDateTimeSerializer;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

/**
 * @author Dariusz Stefanski
 */
@Data
public class LoanResp {

    private Long id;

    private BigDecimal amount;

    private BigDecimal interest;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime start;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime end;

    private List<Extension> extensions;


    public static LoanResp fromLoan(Loan loan) {
        LoanResp resp = new LoanResp();
        resp.setId(loan.getId());
        resp.setAmount(loan.getAmount());
        resp.setInterest(loan.getInterest());
        resp.setStart(loan.getApplicationTime());
        resp.setEnd(loan.getDeadline());
        resp.setExtensions(Collections.unmodifiableList(loan.getExtensions()));
        return resp;
    }
}
