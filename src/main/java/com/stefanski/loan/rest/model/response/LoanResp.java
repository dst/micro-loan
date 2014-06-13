package com.stefanski.loan.rest.model.response;

import com.stefanski.loan.core.domain.Extension;
import com.stefanski.loan.core.domain.Loan;
import lombok.Data;

import java.math.BigDecimal;
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
    //TODO(dst), 6/8/14: add dates
//    private LocalDateTime startDateTime;
//    private LocalDateTime endDateTime;

    private List<Extension> extensions;

    public static LoanResp fromLoan(Loan loan) {
        LoanResp resp = new LoanResp();
        resp.setId(loan.getId());
        resp.setAmount(loan.getAmount());
        resp.setInterest(loan.getInterest());
        resp.setExtensions(Collections.unmodifiableList(loan.getExtensions()));
        return resp;
    }
}
