package com.digitalmoney.accountservice.repository.feign;

import com.digitalmoney.accountservice.entity.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "user-service", url = "localhost:8087/users")
public interface IUserFeignRepository {

    @GetMapping("/{id}")
    User getCvcAndAliasByUserId(@PathVariable(value = "id") Integer id);

    @PutMapping("/user/update/{id}")
    User editAlias(@PathVariable(value = "id") Integer id, @RequestBody User user);
}
