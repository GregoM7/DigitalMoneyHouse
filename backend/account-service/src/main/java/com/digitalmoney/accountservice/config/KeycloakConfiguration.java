package com.digitalmoney.accountservice.config;

import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "keycloak")
public class KeycloakConfiguration {

    @Value("${dh.keycloak.serverUrl}")
    private String serverUrl;

    @Value("${dh.keycloak.realm}")
    private String realm;

    @Value("${dh.keycloak.clientId}")
    private String clientId;

    @Value("${dh.keycloak.clientSecret}")
    private String clientSecret;

    @Bean
    public Keycloak buildClient(){
        return KeycloakBuilder
                .builder()
                .serverUrl(serverUrl)
                .realm(realm)
                .clientId(clientId)
                .clientSecret(clientSecret)
                .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
                .resteasyClient(new ResteasyClientBuilder().connectionPoolSize(10).build())
                .build();
    }

}
