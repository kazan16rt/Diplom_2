import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;

public class BaseClient {
    private static final String URL = "https://stellarburgers.nomoreparties.site";

    public RequestSpecification getSpec() {
        return new RequestSpecBuilder()
                .addHeader("Content-Type", "application/json")
                .setBaseUri(URL)
                .build();
    }
}
