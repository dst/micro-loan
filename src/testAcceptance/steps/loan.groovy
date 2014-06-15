import com.stefanski.loan.core.domain.Customer
import com.stefanski.loan.rest.model.request.LoanRequest
import wslite.rest.RESTClient
import wslite.rest.Response

import java.time.LocalDate

import static cucumber.api.groovy.EN.*
import static cucumber.api.groovy.Hooks.Before
import static java.time.format.DateTimeFormatter.ISO_DATE

/**
 * @author Dariusz Stefanski
 */

DATE_FORMAT = /\d{4}-\d{2}-\d{2}/

def customer
def loan
def loanReq

Before() {
    client = new RESTClient(Helper.SERVER_ADDRESS)
}

Given(~'^customer$') { ->
    customer = new Customer('John', 'Smith')
    response = createCustomer(customer)
    assert response.statusCode == 201
    customerId = response.json.id
    assert customerId > 0
}

Given(~'^customer has loan$') { ->
    loanReq = new LoanRequest(customerId, 1000, 30)
    response = createLoan(loanReq)
    assert response.statusCode == 201
    loanId = response.json.id
    loanLocation = getLocation(response)
    loan = getJson(loanLocation)
}

Given(~'^loan has extension$') { ->
    loanPath = location2path(loanLocation)
    response = client.post(path: "${loanPath}/extensions") {}
    assert response.statusCode == 201
    extensionId = response.json.id
    assert extensionId > 0
}

When(~'^customer wants to loan (\\d+) PLN for (\\d+) days$') { int amount, int daysCount ->
    loanReq = new LoanRequest(customerId, amount, daysCount)
    response = createLoan(loanReq)
}

When(~'^customer extends loan$') { ->
    loanPath = location2path(loanLocation)
    response = client.post(path: "${loanPath}/extensions") {}
    assert response.statusCode == 201
    extensionId = response.json.id
    assert extensionId > 0
}

When(~'^customer wants to see his loan$') { ->
    loan = getJson(loanLocation)
}

When(~'^customer wants to see his loans$') { ->
    loans = getJson("/loans?customerId=${customerId}")
}

Then(~'^loan is issued$') { ->
    assert response.statusCode == 201
    loanId = response.json.id
    assert loanId > 0
}

Then(~'^interest gets increased by factor of ([^"]*)$') { BigDecimal factor ->
    oldInterest = loan.interest

    // Get updated loan
    extendedLoan = getJson(loanLocation)
    newInterest = extendedLoan.interest

    assert newInterest == oldInterest * factor
}

Then(~'^term is extended for (\\d+) days$') { int daysCount ->
    // Get extended loan
    extendedLoan = getJson(loanLocation)

    oldEnd = LocalDate.parse(loan.end, ISO_DATE)
    newEnd = LocalDate.parse(extendedLoan.end, ISO_DATE)

    assert oldEnd.plusDays(daysCount) == newEnd
}

Then(~'^he can see loan$') { ->
    assert loan.id == loanId
    assert loan.amount == loanReq.amount
    assert loan.interest > 0
    assert loan.start ==~ DATE_FORMAT
    assert loan.end ==~ DATE_FORMAT
}

Then(~'^he can see extension$') { ->
    assert loan.extensions
    assert loan.extensions.size() == 1
    assert loan.extensions[0].id == extensionId
    assert loan.extensions[0].creationTime ==~ DATE_FORMAT
}

Then(~'^he can see (\\d+) loans$') { int loanCount ->
    assert loans.size() == 2
}

private Response createCustomer(Customer customer) {
    client.post(path: '/customers') {
        json firstName: customer.firstName, lastName: customer.lastName
    }
}

private Response createLoan(LoanRequest loanReq) {
    client.post(path: "/loans") {
        json customerId: customerId, amount: loanReq.amount, daysCount: loanReq.daysCount
    }
}

private String getLocation(Response response) {
    location = response.getHeaders().get('location')
    assert location
    location
}

private Object getJson(String location) {
    path = location2path(location)
    response = client.get(path: path)
    assert response.statusCode == 200
    return response.json
}

/**
 * Get rid off server part from location
 *
 * @param location
 * @return path without server address
 */
private String location2path(String location) {
    return location.replaceAll(Helper.SERVER_ADDRESS, "")
}