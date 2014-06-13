import com.stefanski.loan.core.domain.Customer
import wslite.rest.Response

import static cucumber.api.groovy.EN.*

/**
 * @author Dariusz Stefanski
 */

def customer

Given(~'^customer with firstName "([^"]*)" and lastName "([^"]*)"$') { String firstName, String lastName ->
    customer = new Customer(firstName, lastName)
}

When(~'^creation is performed$') { ->
    response = createCustomer(customer)
}

Then(~'^customer is created$') { ->
    assert response.statusCode == 201
    id = response.json.id
    assert id > 0
    customer.id = id
}

Given(~'^existing customer$') { ->
    customer = new Customer('John', 'Smith')
    response = createCustomer(customer)
    assert response.statusCode == 201
    customer.id = response.json.id
}

When(~'^customer is asked$') { ->
    response = client.get(path: "/customers/${customer.id}");
}

Then(~'^customer is returned$') { ->
    assert response.statusCode == 200
    json = response.json
    assert json.firstName == customer.firstName
    assert json.lastName == customer.lastName
}

private Response createCustomer(Customer customer) {
    return client.post(path: '/customers') {
        json firstName: customer.firstName, lastName: customer.lastName
    }
}
