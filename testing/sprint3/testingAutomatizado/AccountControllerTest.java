import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;

public class AccountControllerTest {

    @BeforeEach
    public void setUp() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 8080; 
    }

    @Test
    public void testGetAccountDetailsById() {
        Integer accountId = 1;

        given()
                .pathParam("id", accountId)
                .when()
                .get("/accounts/{id}")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("accountId", equalTo(accountId))
                .body("balance", notNullValue());
    }

    @Test
    public void testGetActivity() {
        Integer accountId = 1;

        given()
                .pathParam("id", accountId)
                .when()
                .get("/accounts/{id}/activity")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("$", hasSize(5)); 
    }

    @Test
    public void testCreateAccount() {
        CreateAccountRequest request = new CreateAccountRequest();
        request.setUserId(123);

        given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/accounts/create")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("accountId", notNullValue())
                .body("userId", equalTo(123));
    }
    @Test
    public void testUpdateAccount() {
        Integer accountId = 1;
        Account account = new Account();

        given()
                .pathParam("id", accountId)
                .contentType(ContentType.JSON)
                .body(account)
                .when()
                .put("/accounts/{id}")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("accountId", equalTo(accountId))
    }

    @Test
    public void testPatchAccount() {
        Integer accountId = 1;
        Account account = new Account();
        

        given()
                .pathParam("id", accountId)
                .contentType(ContentType.JSON)
                .body(account)
                .when()
                .patch("/accounts/{id}")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("accountId", equalTo(accountId))
    }

    @Test
    public void testGetLastFiveByAccountId() {
        Integer accountId = 1;

        given()
                .pathParam("id", accountId)
                .when()
                .get("/accounts/{id}/transactions")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("$", hasSize(5)) 
                .body("accountId", everyItem(equalTo(accountId))); 
    
    }
}
