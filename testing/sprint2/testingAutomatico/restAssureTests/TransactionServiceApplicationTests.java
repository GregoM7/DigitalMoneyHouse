package com.digitalmoney.transactionservice;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import io.restassured.RestAssured;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
@SpringBootTest
class TransactionServiceApplicationTests {



	@RunWith(SpringRunner.class)
	@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
	public class TransactionControllerTest {



		@Before
		public void setup() {
			RestAssured.baseURI = "http://localhost";
			RestAssured.port = 8080; // Configura el puerto en el que se ejecuta tu aplicación
		}

		@Test
		public void getAllTransactionsTest() {
			given()
					.when()
					.get("/transactions")
					.then()
					.statusCode(HttpStatus.OK.value())
					.body(not(empty()));
		}

		@Test
		public void getByIdTest() {
			given()
					.pathParam("id", 1)
					.when()
					.get("/transactions/{id}")
					.then()
					.statusCode(HttpStatus.OK.value())
					.body("id", equalTo(1));
		}

	}


}
