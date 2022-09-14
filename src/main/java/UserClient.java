import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;

public class UserClient extends BaseClient {
    private static final String USER_REGISTER_PATH = "/api/auth/register";
    private static final String USER_LOGIN_PATH = "/api/auth/login";
    private static final String USER_UPDATE_PATH = "/api/auth/user";
    private static final String USER_DELETE_PATH = "/api/auth/user";

    public ValidatableResponse register(User user) {
        return given()
                .log().all()
                .spec(getSpec())
                .body(user)
                .when()
                .post(USER_REGISTER_PATH)
                .then()
                .log().all();
    }
    public ValidatableResponse login(UserCredentials userCredentials) {
        return given()
                .spec(getSpec())
                .log().all()
                .body(userCredentials)
                .when()
                .post(USER_LOGIN_PATH)
                .then()
                .log().all();
    }
    public ValidatableResponse delete(String token) {
        return given()
                .log().all()
                .spec(getSpec())
                .header("Authorization",
                        token)
                .when()
                .delete(USER_UPDATE_PATH)
                .then()
                .log().all();
    }
    public ValidatableResponse updateUser(User user) {
        return given()
                .log().all()
                .spec(getSpec())
                .body(user)
                .when()
                .patch(USER_UPDATE_PATH)
                .then()
                .log().all();
    }
    public ValidatableResponse updateUser(User user, String token) {
        return given()
                .log().all()
                .spec(getSpec())
                .header("Authorization",
                        token)
                .body(user)
                .when()
                .patch(USER_DELETE_PATH)
                .then()
                .log().all();
    }
}
