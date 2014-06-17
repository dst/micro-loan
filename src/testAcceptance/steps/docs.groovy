import wslite.rest.RESTClient

import static cucumber.api.groovy.EN.Then
import static cucumber.api.groovy.EN.When
import static cucumber.api.groovy.Hooks.Before

/**
 * @author Dariusz Stefanski
 */


Before() {
    client = new RESTClient(Helper.SERVER_ADDRESS)
}

When(~'^somebody visit main page$') { ->
    response = client.get(path: "")
}

Then(~'^Swagger documentation is visible$') { ->
    assert response.contentAsString.contains("Swagger UI")
}
