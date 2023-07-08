package com.digitalmedia.users;

import com.digitalmedia.users.config.KeycloakConfiguration;
import com.digitalmedia.users.service.UserService;
import com.digitalmedia.users.service.UserVerificationService;
import org.junit.jupiter.api.Test;
import org.keycloak.admin.client.Keycloak;
import org.springframework.boot.test.context.SpringBootTest;
import io.restassured.RestAssured;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit4.SpringRunner;
import io.restassured.http.ContentType;
import org.springframework.http.HttpStatus;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
@SpringBootTest
class UsersServiceApplicationTests {
  @RunWith(SpringRunner.class)
  @SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
  public class KeycloakConfigurationTest {

    @Value("${dh.keycloak.serverUrl}")
    private String serverUrl;

    @Autowired
    private KeycloakConfiguration keycloakConfiguration;

    @Before
    public void setup() {
      RestAssured.baseURI = serverUrl;
    }

    @Test
    public void keycloakBuildClientTest() {
      Keycloak keycloak = keycloakConfiguration.buildClient();

    }
  }

  @Before
  public void setup() {
    RestAssured.baseURI = "http://localhost";
    RestAssured.port = 8080;
  }

  @Test
  public void passwordRecoverTest() {
    given()
            .pathParam("mail", "test@example.com")
            .when()
            .get("/passwordRecover/{mail}")
            .then()
            .statusCode(HttpStatus.OK.value())
            .body(is(emptyString()));
  }

  @Test
  public void getUserTest() {
    given()
            .pathParam("id", 1)
            .when()
            .get("/{id}")
            .then()
            .statusCode(HttpStatus.OK.value())
            .body("id", equalTo(1));
  }

  @Test
  public void patchAliasTest() {
    given()
            .contentType(ContentType.JSON)
            .body("{\"alias\": \"newAlias\"}")
            .when()
            .patch("/alias")
            .then()
            .statusCode(HttpStatus.OK.value())
            .body(is(equalTo("Actualizado con exito")));
  }

}

