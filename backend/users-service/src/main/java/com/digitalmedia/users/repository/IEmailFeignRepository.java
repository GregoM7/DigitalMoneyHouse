package com.digitalmedia.users.repository;

import com.digitalmedia.users.model.EmailRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "email-service", url = "localhost:8088/email")
public interface IEmailFeignRepository {

    @PostMapping()
    public ResponseEntity<?> sendEmail(@RequestBody EmailRequest emailRequest);
}
