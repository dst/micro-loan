package com.stefanski.loan.rest.model.response;

import com.stefanski.loan.core.domain.Loan;
import lombok.Data;

import java.math.BigDecimal;

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

    public static LoanResp fromLoan(Loan loan) {
        LoanResp resp = new LoanResp();
        resp.setId(loan.getId());
        resp.setAmount(loan.getAmount());
        resp.setInterest(loan.getInterest());
        return resp;
    }
}
