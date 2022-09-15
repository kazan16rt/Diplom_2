import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;

public class OrderClient extends BaseClient {
    private final static String GET_USER_ORDERS_PATH = "/api/orders";
    private final static String CREATE_ORDER_PATH = "/api/orders";

    @Step("Send POST request to {GET_USER_ORDERS_PATH}")
    public ValidatableResponse myOrders() {
        return given()
                .log().all()
                .spec(getSpec())
                .when()
                .get(GET_USER_ORDERS_PATH)
                .then()
                .log().all();
    }
    @Step("Send GET request to {GET_USER_ORDERS_PATH}")
    public ValidatableResponse myOrders(String token) {
        return given()
                .log().all()
                .spec(getSpec())
                .header("Authorization",
                        token)
                .when()
                .get(GET_USER_ORDERS_PATH)
                .then()
                .log().all();
    }
    @Step("Send POST request to {CREATE_ORDER_PATH}")

    public ValidatableResponse createOrder(Order order, String token) {
        return given()
                .log().all()
                .spec(getSpec())
                .header("Authorization",
                        token)
                .body(order)
                .when()
                .post(CREATE_ORDER_PATH)
                .then()
                .log().all();
    }
    @Step("Send POST request to {CREATE_ORDER_PATH}")
    public ValidatableResponse createOrder(Order order) {
        return given()
                .log().all()
                .spec(getSpec())
                .body(order)
                .when()
                .post(CREATE_ORDER_PATH)
                .then()
                .log().all();
    }
}
