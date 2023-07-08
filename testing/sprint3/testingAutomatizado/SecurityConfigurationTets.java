import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.web.server.SecurityWebFilterChain;

import static io.restassured.RestAssured.given;
import static org.mockito.Mockito.when;

@EnableWebFluxSecurity
public class SecurityConfigurationTest {

    @Mock
    private ReactiveClientRegistrationRepository mockClientRegistrationRepository;

    private SecurityConfiguration securityConfiguration;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        securityConfiguration = new SecurityConfiguration(mockClientRegistrationRepository);

        // Configurar RestAssured
        RestAssured.filters(securityConfiguration.springSecurityFilterChain(null).getWebFilters().collectList().block());
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 8080;
    }
