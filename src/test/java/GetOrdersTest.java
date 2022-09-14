import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.junit.Assert.*;

public class GetOrdersTest {
    private User user;
    private UserClient userClient;
    private Order order;
    private OrderClient orderClient;
    private String token;

    @Before
    public void setUp() {
        user = UserData.getDefault();
        userClient = new UserClient();
        orderClient = new OrderClient();
        order = OrderData.getDefault();
    }

    @After
    public void tearDown() {
        if(token != null) {
            userClient.delete(token);
        }
    }
    @Test
    public void getUserOrdersWithoutTokenTest() {
        ValidatableResponse response = orderClient.myOrders();
        int statusCode = response.extract().statusCode();
        assertEquals("Status code is incorrect", SC_UNAUTHORIZED, statusCode);
        String message = response.extract().path("message");
        assertEquals("User without authorization got orders", UserErrors.UPDATE_UNAUTHORIZED_USER, message);
    }

    @Test
    public void getUserOrdersTest() {
        ValidatableResponse register = userClient.register(user);
        token = register.extract().path("accessToken");
        orderClient.createOrder(order, token);
        ValidatableResponse myOrders = orderClient.myOrders(token);

        int statusCode = myOrders.extract().statusCode();
        assertEquals("Status code is incorrect", SC_OK, statusCode);

        boolean success = myOrders.extract().path("success");
        assertTrue("Status is not expected", success);

        List<String> orders = myOrders.extract().path("orders");
        assertNotNull("Orders is missing", orders);
    }
}
