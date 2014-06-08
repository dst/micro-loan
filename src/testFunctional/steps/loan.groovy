import wslite.rest.RESTClient

import static cucumber.api.groovy.EN.*
import static cucumber.api.groovy.Hooks.Before

Before() {
    client = new RESTClient("http://localhost:8888")
}

Given(~'^a customer$') { ->
    //TODO(dst), 6/8/14: better way of creating customer
    response = client.post(path: '/customers') {
        json firstName: 'John', lastName: 'Smith'
    }
    assert response.statusCode == 201
    customerId = response.json.id
}

When(~'^a customer wants to loan (\\d+) PLN for (\\d+) days$') { int amount, int daysCount ->
    response = client.post(path: "/customers/$customerId/loans") {
        json amount: amount, daysCount: daysCount
    }
}

Then(~'^a loan is issued$') { ->
    assert response.statusCode == 201
    loanId = response.json.id
    assert loanId > 0
}