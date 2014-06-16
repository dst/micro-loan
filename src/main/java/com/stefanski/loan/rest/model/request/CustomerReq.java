package com.stefanski.loan.rest.model.request;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * @author Dariusz Stefanski
 */
@Data
@ApiModel
public class CustomerReq {

    @NotEmpty
    @ApiModelProperty(required = true)
    private String firstName;

    @NotEmpty
    @ApiModelProperty(required = true)
    private String lastName;

    public CustomerReq() {
    }

    public CustomerReq(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }
}
