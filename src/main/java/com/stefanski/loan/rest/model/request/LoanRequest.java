package com.stefanski.loan.rest.model.request;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @author Dariusz Stefanski
 */
@Data
public class LoanRequest {
    @NotNull
    private Long customerId;

    @NotNull
    private BigDecimal amount;

    @NotNull
    private Integer daysCount;

    // Client should not fill this. It will be overridden by controller.
    private String ip;

    public LoanRequest() {
    }

    public LoanRequest(Long customerId, BigDecimal amount, Integer daysCount) {
        this.customerId = customerId;
        this.amount = amount;
        this.daysCount = daysCount;
    }
}
