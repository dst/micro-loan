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
    private BigDecimal amount;

    @NotNull
    private Integer daysCount;
}