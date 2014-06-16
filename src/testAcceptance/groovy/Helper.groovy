import com.stefanski.loan.rest.model.request.CustomerReq
import wslite.rest.RESTClient
import wslite.rest.Response

/**
 * @author Dariusz Stefanski
 */
class Helper {
    public static final String SERVER_ADDRESS = "http://localhost:8888"

    public static Object getJson(RESTClient client, String location) {
        def path = location2path(location)
        def response = client.get(path: path)
        assert response.statusCode == 200
        return response.json
    }

    public static String getLocation(Response response) {
        def location = response.getHeaders().get("location")
        assert location
        return location
    }

    /**
     * Get rid off server part from location
     *
     * @param location
     * @return path without server address
     */
    public static String location2path(String location) {
        return location.replaceAll(SERVER_ADDRESS, "")
    }

    public static Long createCustomer(RESTClient client, CustomerReq customer) {
        def response = client.post(path: '/customers') {
            json firstName: customer.firstName, lastName: customer.lastName
        }
        assert response.statusCode == 201
        def id = response.json.id
        assert id > 0
        return id
    }


}
