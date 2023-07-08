package com.digitalmoney.cardservice.repository.feign;

import com.digitalmoney.cardservice.entity.Account;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "account-service", url = "localhost:8089/accounts")
public interface IAccountFeignRepository {

    @GetMapping("/{id}")
    Account getAccountById(@PathVariable(value = "id") Integer id);

}