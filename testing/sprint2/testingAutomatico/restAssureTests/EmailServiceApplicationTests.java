package com.dmh.email.service;

import com.dmh.email.service.model.EmailRequest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.BeforeClass;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import static org.assertj.core.api.BDDAssumptions.given;

class EmailServiceApplicationTests {

@SpringBootTest



	public class EmailControllerTest {

		@BeforeClass
		public static void setup() {
			RestAssured.baseURI = "http://localhost";
			RestAssured.port = 8080; // Establecer el puerto de la API
		}

		@Test
		public void sendEmailTest() {
			EmailRequest email = new EmailRequest();
			email.setToEmail("recipient@example.com");
			email.setSubject("Test Subject");
			email.setText("Test Email Content");

			given()
					.contentType(ContentType.JSON)
					.body(email)
					.when()
					.post("/email")
					.then()
					.statusCode(200);
		}
	}


}
