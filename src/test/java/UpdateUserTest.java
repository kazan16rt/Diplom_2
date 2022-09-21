import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.junit.Assert.*;

public class UpdateUserTest {
    private User defaultUser;
    private User changedUser;
    private UserClient userClient;
    private String token;

    @Before
    public void setUp() {
        defaultUser = UserData.getDefault();
        changedUser = UserData.getChangedUser();

        userClient = new UserClient();
    }

    @After
    public void tearDown() {
        if(token != null) {
            userClient.delete(token);
        }
    }

    @Test
        public void updateUserWithoutAuthoriseFailedTest() {
        ValidatableResponse response = userClient.updateUser(defaultUser);

        int statusCode = response.extract().statusCode();
        assertEquals("Status code is incorrect", SC_UNAUTHORIZED, statusCode);

        boolean success = response.extract().path("success");
        assertFalse("User without authorization is changed", success);

        String message = response.extract().path("message");
        assertEquals("User without authorization is changed", UserErrors.UPDATE_UNAUTHORIZED_USER, message);
    }

    @Test
    public void updateUserSuccessTest() {
        ValidatableResponse response = userClient.register(defaultUser);
        token = response.extract().path("accessToken");
        ValidatableResponse updateResponse = userClient.updateUser(changedUser, token);

        int statusCode = updateResponse.extract().statusCode();
        assertEquals("Status code is incorrect", SC_OK, statusCode);

        boolean success = updateResponse.extract().path("success");
        assertTrue("User is not changed", success);

        String email = updateResponse.extract().path("user.email");
        assertEquals("Email is not changed", changedUser.getEmail(), email);

        String name = updateResponse.extract().path("user.name");
        assertEquals("Name is not changed", changedUser.getName(), name);

        ValidatableResponse reloginResponse = userClient.login(UserCredentials.from(changedUser));
        int statusCodeRelogin = reloginResponse.extract().statusCode();
        assertEquals("Status code is incorrect", SC_OK, statusCode);

        token = reloginResponse.extract().path("accessToken");
        Boolean successRelogin = reloginResponse.extract().path("success");
        assertTrue("Relogin failed", successRelogin);
    }
}
