import com.stefanski.loan.rest.model.request.CustomerReq

import static cucumber.api.groovy.EN.Given

/**
 * @author Dariusz Stefanski
 */

Given(~'^existing customer$') { ->
    customerReq = new CustomerReq('John', 'Smith')
    customerId = Helper.createCustomer(client, customerReq)
}
