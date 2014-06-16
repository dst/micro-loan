import com.stefanski.loan.rest.model.request.LoanReq
import wslite.rest.RESTClient
import wslite.rest.Response

import java.time.LocalDate

import static Helper.SERVER_ADDRESS
import static cucumber.api.groovy.EN.*
import static cucumber.api.groovy.Hooks.Before
import static java.time.format.DateTimeFormatter.ISO_DATE

/**
 * @author Dariusz Stefanski
 */

DATE_FORMAT = /\d{4}-\d{2}-\d{2}/

def loan

Before() {
    client = new RESTClient(SERVER_ADDRESS)
}

Given(~'^customer has loan$') { ->
    loanReq = new LoanReq(customerId, 1000, 30)
    response = createLoan(loanReq)
    assert response.statusCode == 201
    loanId = response.json.id
    assert loanId > 0
    loanLocation = Helper.getLocation(response)
    loan = Helper.getJson(client, loanLocation)
}

Given(~'^loan has extension$') { ->
    loanPath = Helper.location2path(loanLocation)
    response = client.post(path: "${loanPath}/extensions") {}
    assert response.statusCode == 201
    extensionId = response.json.id
    assert extensionId > 0
}

When(~'^customer wants to loan (\\d+) PLN for (\\d+) days$') { int amount, int daysCount ->
    loanReq = new LoanReq(customerId, amount, daysCount)
    response = createLoan(loanReq)
}

When(~'^customer extends loan$') { ->
    loanPath = Helper.location2path(loanLocation)
    response = client.post(path: "${loanPath}/extensions") {}
    assert response.statusCode == 201
    extensionId = response.json.id
    assert extensionId > 0
}

When(~'^customer wants to see his loan$') { ->
    loan = Helper.getJson(client, loanLocation)
}

When(~'^customer wants to see his loans$') { ->
    loans = Helper.getJson(client, "/loans?customerId=${customerId}")
}

Then(~'^loan is issued$') { ->
    assert response.statusCode == 201
    loanId = response.json.id
    assert loanId > 0
}

Then(~'^interest gets increased by factor of ([^"]*)$') { BigDecimal factor ->
    oldInterest = loan.interest

    // Get updated loan
    extendedLoan = Helper.getJson(client, loanLocation)
    newInterest = extendedLoan.interest

    assert newInterest == oldInterest * factor
}

Then(~'^term is extended for (\\d+) days$') { int daysCount ->
    // Get extended loan
    extendedLoan = Helper.getJson(client, loanLocation)

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

private Response createLoan(LoanReq loanReq) {
    client.post(path: "/loans") {
        json customerId: customerId, amount: loanReq.amount, daysCount: loanReq.daysCount
    }
}
