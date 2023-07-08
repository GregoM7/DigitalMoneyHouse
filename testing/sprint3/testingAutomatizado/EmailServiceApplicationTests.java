package com.dmh.email.service;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import static io.restassured.RestAssured.given;
import static org.mockito.Mockito.when;

public class EmailConfigurationTest {

    @Mock
    private JavaMailSender mockJavaMailSender;

    private EmailConfiguration emailConfiguration;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        emailConfiguration = new EmailConfiguration();

        // Configurar RestAssured para capturar las solicitudes salientes
        RestAssured.replaceObjectMapper(null);
        RestAssured.filters((req, res, ctx) -> {
            mockJavaMailSender.send(req);
            return ctx.next(req, res);
        });
    }