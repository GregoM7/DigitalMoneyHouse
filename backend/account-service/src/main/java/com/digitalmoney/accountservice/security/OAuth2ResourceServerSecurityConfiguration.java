package com.digitalmoney.accountservice.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class OAuth2ResourceServerSecurityConfiguration {

  @Value("${spring.security.oauth2.client.provider.keycloak.issuer-uri}")
  private String baseUrl;

  @Bean
  protected SecurityFilterChain filterChain (HttpSecurity http) throws Exception {
    http.oauth2ResourceServer().jwt().jwtAuthenticationConverter(new KeyCloakJwtAuthenticationConverter());
    http.cors().and().csrf().disable()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
            .authorizeRequests()
            .antMatchers("/accounts/{id}").permitAll()
            .antMatchers(HttpMethod.GET, "accounts/{id}/activity").authenticated()
            .antMatchers(HttpMethod.PATCH,"/{id}").authenticated()
            .antMatchers(HttpMethod.GET,"accounts/{id}/transactions").permitAll()
            .antMatchers("accounts/{id}/transferences").authenticated()
            .antMatchers(HttpMethod.GET, "accounts/{id}/activity/{transactionId}").authenticated()
            .antMatchers("/accounts/{id}/transferences").authenticated()
            .antMatchers("/accounts/{id}/lastAccounts").authenticated();
    return http.build();
  }

  @Bean
  public JwtDecoder jwtDecoder() {
    return NimbusJwtDecoder.withJwkSetUri(baseUrl.concat("/protocol/openid-connect/certs")).build();
  }
}