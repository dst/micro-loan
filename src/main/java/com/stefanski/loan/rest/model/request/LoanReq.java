package com.stefanski.loan.rest.model.request;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @author Dariusz Stefanski
 */
@Data
@ApiModel
public class LoanReq {
    @NotNull
    @ApiModelProperty(required = true)
    private Long customerId;

    @NotNull
    @ApiModelProperty(value = "how much money to loan", required = true)
    private BigDecimal amount;

    @NotNull
    @ApiModelProperty(value ="how many days", required = true)
    private Integer daysCount;

    // Client should not fill this. It will be overridden by controller.
    @ApiModelProperty(hidden = true)
    private String ip;

    public LoanReq() {
    }

    public LoanReq(Long customerId, BigDecimal amount, Integer daysCount) {
        this.customerId = customerId;
        this.amount = amount;
        this.daysCount = daysCount;
    }
}
