package com.digitalmoney.cardservice;

import com.digitalmoney.cardservice.entity.Card;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.BeforeClass;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static sun.jvm.hotspot.utilities.AddressOps.greaterThan;


@SpringBootTest
class CardServiceApplicationTests {

	public class CardServiceTest {

		@BeforeClass
		public static void setup() {
			RestAssured.baseURI = "http://localhost:8080"; // Establecer la URL base de la API
		}

		@Test
		public void getAllByAccountIdTest() {
			given()
					.pathParam("id", 1) // Establecer el valor del parámetro de ruta
					.when()
					.get("/cards/accounts/{id}")
					.then()
					.statusCode(200) // Verificar el código de estado de respuesta
					.contentType(ContentType.JSON) // Verificar el tipo de contenido de respuesta
					.body("$", hasSize(2)); // Verificar que la respuesta contenga una lista de 2 elementos
		}

		@Test
		public void createCardTest() {
			Card card = new Card();
			// Establecer los datos de la tarjeta necesarios

			given()
					.contentType(ContentType.JSON)
					.body(card) // Establecer el cuerpo de la solicitud
					.when()
					.post("/cards")
					.then()
					.statusCode(200)
					.contentType(ContentType.JSON)
					.body("property", equalTo("expectedValue")); // Verificar el valor de una propiedad de la tarjeta en la respuesta
		}

		@Test
		public void deleteCardTest() {
			given()
					.pathParam("accountId", 1) // Establecer el valor del parámetro de ruta
					.pathParam("id", 1) // Establecer el valor del parámetro de ruta
					.when()
					.delete("/cards/accounts/{accountId}/{id}")
					.then()
					.statusCode(200);
		}

		@Test
		public void getAccountByIdTest() {
			given()
					.pathParam("accountOriginId", 1)
					.when()
					.get("/accounts/{accountOriginId}")
					.then()
					.statusCode(200)
					.contentType(ContentType.JSON)
					.body("property", equalTo("expectedValue"));
		}
	}

	@Test
	public void authenticateWithInvalidCredentialsTest() {
		given()
				.param("username", "invalidUser")
				.param("password", "invalidPassword")
				.when()
				//.post("") apí
				.then()
				.statusCode(401);
	}

	@Test
	public void getAllByAccountIdTest() {
		given()
				.pathParam("id", 1)
				.when()
				.get("/cards/account/{id}")
				.then()
				.statusCode(200)
				.contentType(ContentType.JSON)
				.body("$", hasSize(greaterThan(0)));
	}

	@Test
	public void createCardTest() {
		Card card = new Card();

		given()
				.contentType(ContentType.JSON)
				.body(card)
				.when()
				.post("/cards")
				.then()
				.statusCode(200)
				.contentType(ContentType.JSON)
				.body("id", notNullValue());
	}

	@Test
	public void deleteCardTest() {
		given()
				.pathParam("accountId", 1)
				.pathParam("cardId", 1)
				.when()
				.delete("/cards/account/{accountId}/{cardId}")
				.then()
				.statusCode(200);
	}

	@Test
	public void getAccountByIdTest() {
		given()
				.pathParam("accountOriginId", 1)
				.when()
				.get("/cards/account/{accountOriginId}/account")
				.then()
				.statusCode(200)
				.contentType(ContentType.JSON)
				.body("id", equalTo(1));
	}
}




