import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.apache.http.HttpStatus.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class UserRegisterTest {
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
        userClient.delete(token);
    }

    @Test
    public void userCanBeCreateTest() {
        ValidatableResponse response = userClient.register(user);

        int statusCode = response.extract().statusCode();
        assertEquals("Status code is incorrect", SC_OK, statusCode);

        boolean isCreated = response.extract().path("success");
        assertTrue("User is not created", isCreated);

        ValidatableResponse loginResponse = userClient.login(UserCredentials.from(user));
        int loginStatusCode = loginResponse.extract().statusCode();
        assertEquals("Status code is incorrect", SC_OK, loginStatusCode);

        token = loginResponse.extract().path("accessToken");
        Boolean success = loginResponse.extract().path("success");
        assertTrue("Login failed", success);
    }

    @Test
    public void duplicateUserCantBeCreate() {
        ValidatableResponse response = userClient.register(user);

        int statusCode = response.extract().statusCode();
        assertEquals("Status code is incorrect", SC_OK, statusCode);

        boolean isCreated = response.extract().path("success");
        assertTrue("User is not created", isCreated);

        ValidatableResponse loginResponse = userClient.login(UserCredentials.from(user));
        int loginStatusCode = loginResponse.extract().statusCode();
        assertEquals("Status code is incorrect", SC_OK, loginStatusCode);

        token = loginResponse.extract().path("accessToken");
        Boolean success = loginResponse.extract().path("success");
        assertTrue("Login failed", success);

        ValidatableResponse secondUser = userClient.register(user);
        int secondLoginStatusCode = secondUser.extract().statusCode();
        assertEquals("Status code is incorrect", SC_FORBIDDEN, secondLoginStatusCode);

        Boolean secondSuccess = secondUser.extract().path("success");
        assertTrue("Duplicate user created", !secondSuccess);
        String message = secondUser.extract().path("message");
        assertEquals("Duplicate user created", UserErrors.CREATE_DUPLICATE_USER, message);
    }


}
