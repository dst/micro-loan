import com.stefanski.loan.core.domain.Customer
import com.stefanski.loan.rest.model.request.LoanRequest
import wslite.rest.RESTClient
import wslite.rest.Response

import static cucumber.api.groovy.EN.*
import static cucumber.api.groovy.Hooks.Before

/**
 * @author Dariusz Stefanski
 */

SERVER_ADDRESS = "http://localhost:8888"

def customer
def loan
def loanReq

Before() {
    client = new RESTClient(SERVER_ADDRESS)
}

Given(~'^customer$') { ->
    customer = new Customer('John', 'Smith')
    response = createCustomer(customer)
    assert response.statusCode == 201
    customer.id = response.json.id
}

When(~'^customer wants to loan (\\d+) PLN for (\\d+) days$') { int amount, int daysCount ->
    loanReq = new LoanRequest(amount, daysCount)
    response = createLoan(customer, loanReq)
}

Then(~'^loan is issued$') { ->
    assert response.statusCode == 201
    loanId = response.json.id
    assert loanId > 0
}

Given(~'^customer has loan$') { ->
    loanReq = new LoanRequest(1000, 30)
    response = createLoan(customer, loanReq)
    assert response.statusCode == 201
    loanId = response.json.id

    loanLocation = getLocation(response)
    loan = getJson(loanLocation)
}

When(~'^customer extends loan$') { ->
    response = client.post(path: "/customers/${customer.id}/loans/$loanId/extensions") {}
    assert response.statusCode == 201
}

Then(~'^interest gets increased by factor of ([^"]*)$') { BigDecimal factor ->
    oldInterest = loan.interest

    // Get updated loan
    extendedLoan = getJson(loanLocation)
    newInterest = extendedLoan.interest

    assert newInterest == oldInterest * factor
}

Then(~'^term is extended for (\\d+) days$') { int daysCount ->
    //TODO(dst), 6/13/14: impl
}

When(~'^customer wants to see his loan$') { ->
    loan = getJson(loanLocation)
}

Then(~'^he can see loan$') { ->
    assert loan.id == loanId
    assert loan.amount == loanReq.amount
    assert loan.interest > 0
}

Given(~'^loan has extension$') { ->
    response = client.post(path: "/customers/${customer.id}/loans/$loanId/extensions") {}
    assert response.statusCode == 201

    extensionId = response.json.id
}

Then(~'^he can see extension$') { ->
    assert loan.extensions
    assert loan.extensions.size() == 1
    assert loan.extensions[0].id == extensionId
    assert loan.extensions[0].creationTime
}

private Response createCustomer(Customer customer) {
    client.post(path: '/customers') {
        json firstName: customer.firstName, lastName: customer.lastName
    }
}

private Response createLoan(Customer customer, LoanRequest loanReq) {
    client.post(path: "/customers/${customer.id}/loans") {
        json amount: loanReq.amount, daysCount: loanReq.daysCount
    }
}

private String getLocation(Response response) {
    location = response.getHeaders().get('location')
    assert location
    location
}

private Object getJson(String location) {
    // Get rid off server part from location
    path = location.replaceAll(SERVER_ADDRESS, "")
    response = client.get(path: path)
    assert response.statusCode == 200
    return response.json
}