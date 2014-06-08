package com.stefanski.loan.rest.model.response;

import com.stefanski.loan.core.domain.Customer;
import lombok.Data;

/**
 * @author Dariusz Stefanski
 */
@Data
public class CustomerResp {

    private String firstName;
    private String lastName;

    public static CustomerResp fromCustomer(Customer customer) {
        CustomerResp resp = new CustomerResp();
        resp.setFirstName(customer.getFirstName());
        resp.setLastName(customer.getLastName());
        return resp;
    }
}
