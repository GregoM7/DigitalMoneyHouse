package com.digitalmedia.users.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.session.ConcurrentSessionFilter;

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
            .antMatchers("/users/test").authenticated()
            .antMatchers("/users/register").permitAll()
            .antMatchers("/users/login").permitAll()

            .antMatchers(HttpMethod.GET,"/users/{id}").permitAll()
            .antMatchers("/users/logout").authenticated()
            .antMatchers(HttpMethod.PATCH,"/users").authenticated()
            .antMatchers(HttpMethod.PATCH,"/users/password").permitAll()
            .antMatchers(HttpMethod.PATCH,"/users/alias").authenticated();

    return http.build();
  }

  @Bean
  public JwtDecoder jwtDecoder() {
    return NimbusJwtDecoder.withJwkSetUri(baseUrl.concat("/protocol/openid-connect/certs")).build();
  }
}