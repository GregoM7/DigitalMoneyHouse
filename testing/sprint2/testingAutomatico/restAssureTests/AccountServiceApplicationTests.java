package com.digitalmoney.accountservice;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import static org.assertj.core.api.BDDAssumptions.given;

@SpringBootTest
class AccountServiceApplicationTests {

	//@Test
	//void contextLoads() {

		public class AccountControllerTest {

			@Test
			public void getAccountDetailsByIdTest() {
				given()
						.pathParam("id", 1)
						.when()
						.get("https://api.example.com/accounts/{id}")
						.then()
						.statusCode(200);
			}
		}


	}

}
