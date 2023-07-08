package com.digitalmedia.users.repository;

import com.digitalmedia.users.model.EmailRequest;
import com.digitalmedia.users.model.dto.CreateAccountRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "account-service", url = "localhost:8089/accounts")
public interface IAccountFeignRepository {

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody CreateAccountRequest createAccountRequest);
}
