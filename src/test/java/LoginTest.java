import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class LoginTest {
    private User user;
    private UserClient userClient;
    private String token;

    @Before
    public void setUp() {
        user = UserData.getDefault();
        userClient = new UserClient();
    }

    @After
    public void tearDown() {
        if(token != null) {
            userClient.delete(token);
        }
    }

    @Test
    public void loginSuccessTest() {
        userClient.register(user);

        ValidatableResponse response = userClient.login(UserCredentials.from(user));
        int statusCode = response.extract().statusCode();
        assertEquals("Status code is incorrect", SC_OK, statusCode);

        token = response.extract().path("accessToken");
        Boolean success = response.extract().path("success");
        assertTrue("Login failed", success);
    }

    @Test
    public void loginWithWrongCredentialsFailedTest() {
        ValidatableResponse response = userClient.login(UserCredentials.from(user));
        int statusCode = response.extract().statusCode();
        assertEquals("Status code is incorrect", SC_UNAUTHORIZED, statusCode);

        boolean success = response.extract().path("success");
        assertTrue("Success login to non-existent user", !success);

        String actual = response.extract().path("message");
        assertEquals("Success login to non-existent user", UserErrors.LOGIN_ERROR_USER, actual);
    }
}
