package com.digitalmoney.transactionservice.security;

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
            .antMatchers("/transactions/**").permitAll();
           /* .antMatchers(HttpMethod.GET,"/transactions/lastAccount/{accountId}").permitAll()
            .antMatchers(HttpMethod.GET,"/transactions/{id}").authenticated()
            .antMatchers(HttpMethod.GET,"/transactions").authenticated()
            .antMatchers(HttpMethod.GET,"/transactions//{transactionId}/{accountId}").authenticated()
            .antMatchers(HttpMethod.GET,"/transactions/accountId/{accountId}").authenticated()
            .antMatchers(HttpMethod.POST,"/transactions").authenticated()
            .antMatchers(HttpMethod.PUT,"/transactions/{id}").authenticated()
            .antMatchers(HttpMethod.DELETE,"/transactions/{id}").authenticated(); */

    return http.build();
  }

  @Bean
  public JwtDecoder jwtDecoder() {
    return NimbusJwtDecoder.withJwkSetUri(baseUrl.concat("/protocol/openid-connect/certs")).build();
  }
}