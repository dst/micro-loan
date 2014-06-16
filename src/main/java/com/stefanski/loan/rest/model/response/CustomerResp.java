package com.stefanski.loan.rest.model.response;

import com.stefanski.loan.core.domain.Customer;
import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author Dariusz Stefanski
 */
@Data
@ApiModel
public class CustomerResp {

    @ApiModelProperty(value = "unique identifier for the customer")
    private Long id;

    @ApiModelProperty(required = true)
    private String firstName;

    @ApiModelProperty(required = true)
    private String lastName;

    public static CustomerResp fromCustomer(Customer customer) {
        CustomerResp resp = new CustomerResp();
        resp.setFirstName(customer.getFirstName());
        resp.setLastName(customer.getLastName());
        return resp;
    }
}

