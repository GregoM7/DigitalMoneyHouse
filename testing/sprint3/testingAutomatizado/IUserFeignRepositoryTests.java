import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class UserFeignRepositoryTest {

    @BeforeEach
    public void setUp() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 8087;
    }

    @Test
    public void testGetCvcAndAliasByUserId() {
        Integer userId = 1;

        given()
                .pathParam("id", userId)
                .when()
                .get("/users/{id}")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("userId", equalTo(userId))
                .body("cvc", notNullValue())
                .body("alias", notNullValue());
    }

    @Test
    public void testEditAlias() {
        Integer userId = 1;
        User user = new User();
        user.setAlias("NewAlias");

        given()
                .pathParam("id", userId)
                .contentType(ContentType.JSON)
                .body(user)
                .when()
                .put("/users/user/update/{id}")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("userId", equalTo(userId))
                .body("alias", equalTo("NewAlias"));
    }
}
