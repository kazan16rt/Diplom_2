import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.apache.http.HttpStatus.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class OrderCreateTest {
    private User user;
    private UserClient userClient;
    private Order order;
    private Order emptyOrder;
    private Order invalidOrder;
    private OrderClient orderClient;
    private String token;
    private final String doneStatus = "done";
    private final String statusLine = "HTTP/1.1 500 Internal Server Error";

    @Before
    public void setUp() {
        user = UserData.getDefault();
        userClient = new UserClient();
        orderClient = new OrderClient();
        order = OrderData.getDefault();
        emptyOrder = OrderData.getEmptyOrder();
        invalidOrder = OrderData.getInvalidIngrefients();
    }

    @After
    public void tearDown() {
        if(token != null) {
            userClient.delete(token);
        }
    }

    @Test
    public void createOrderWithoutToken() {
        ValidatableResponse response = orderClient.createOrder(order);
        int statusCode = response.extract().statusCode();
        assertEquals("Status code is incorrect", SC_OK, statusCode);

        boolean success = response.extract().path("success");
        assertTrue("Order is not created", success);

        String status = response.extract().path("order.status");
        assertEquals("Status must be missing", null, status);
    }

    @Test
    public void createOrderSuccess() {
        ValidatableResponse register = userClient.register(user);
        token = register.extract().path("accessToken");
        ValidatableResponse responseOrder = orderClient.createOrder(order, token);

        int statusCode = responseOrder.extract().statusCode();
        assertEquals("Status code is incorrect", SC_OK, statusCode);

        boolean success = responseOrder.extract().path("success");
        assertTrue("Order is not created", success);

        String status = responseOrder.extract().path("order.status");
        assertEquals("Status is not expected", doneStatus, status);
    }

    @Test
    public void createOrderWithEmptyIngredients() {
        ValidatableResponse register = userClient.register(user);
        token = register.extract().path("accessToken");
        ValidatableResponse responseOrder = orderClient.createOrder(emptyOrder, token);

        int statusCode = responseOrder.extract().statusCode();
        assertEquals("Status code is incorrect", SC_BAD_REQUEST, statusCode);

        boolean success = responseOrder.extract().path("success");
        assertTrue("Status must be false", !success);

        String message = responseOrder.extract().path("message");
        assertEquals("Error text is not expected", UserErrors.INGREDIENTS_MISSING, message);
    }

    @Test
    public void createOrderWithInvalidIngredients() {
        ValidatableResponse register = userClient.register(user);
        token = register.extract().path("accessToken");
        ValidatableResponse responseOrder = orderClient.createOrder(invalidOrder, token);

        int statusCode = responseOrder.extract().statusCode();
        assertEquals("Status code is incorrect", SC_INTERNAL_SERVER_ERROR, statusCode);

        String statusLine = responseOrder.extract().statusLine();
        assertEquals("Status line is not expected", this.statusLine, statusLine);
    }

}
