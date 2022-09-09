import io.restassured.response.ValidatableResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static org.apache.http.HttpStatus.SC_FORBIDDEN;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(Parameterized.class)
public class RegisterParameterizedTest {

    private User user;
    private UserClient userClient;

    private final String email;
    private final String password;
    private final String name;

    public RegisterParameterizedTest(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
    }

    @Parameterized.Parameters
    public static Object[][] getData() {
        return new Object[][] {
                {null, null, null},
                {"potato@yandex.ru", "12345", null},
                {"potato@yandex.ru", null, "John"},
                {null, "12345", "John"},
                {"", "", ""},
        };
    }
    @Before
    public void setUp() {
        user = UserData.getDefault();
        userClient = new UserClient();
    }

    @Test
    public void registerUserWithoutRequiredFieldsTest() {
        user.setEmail(email);
        user.setPassword(password);
        user.setName(name);

        ValidatableResponse response = userClient.register(user);

        int statusCode = response.extract().statusCode();
        assertEquals("Status code is incorrect", SC_FORBIDDEN, statusCode);

        boolean success = response.extract().path("success");
        assertTrue("User without required fields is created", !success);
        String message = response.extract().path("message");
        assertEquals("User without required fields is created", UserErrors.CREATE_ERROR_USER, message);
    }
}
