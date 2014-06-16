import com.stefanski.loan.rest.model.request.CustomerReq
import wslite.rest.RESTClient

import static cucumber.api.groovy.EN.*
import static cucumber.api.groovy.Hooks.Before

/**
 * @author Dariusz Stefanski
 */

Before() {
    client = new RESTClient(Helper.SERVER_ADDRESS)
}

Given(~'^customer with firstName "([^"]*)" and lastName "([^"]*)"$') { String firstName, String lastName ->
    customerReq = new CustomerReq(firstName, lastName)
}

When(~'^creation is performed$') { ->
    response = client.post(path: '/customers') {
        json firstName: customerReq.firstName, lastName: customerReq.lastName
    }
}

When(~'^customer is asked$') { ->
    response = client.get(path: "/customers/" + customerId);
}

Then(~'^customer is created$') { ->
    assert response.statusCode == 201
    id = response.json.id
    assert id > 0
}

Then(~'^customer is returned$') { ->
    assert response.statusCode == 200
    json = response.json
    assert json.firstName == customerReq.firstName
    assert json.lastName == customerReq.lastName
}
