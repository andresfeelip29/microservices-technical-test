package com.co.neoristest.accounts.client;

import com.co.neoristest.accounts.domain.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "microservice-client", url = "${microservices.client.url}/clientes")
public interface UserFeignRequest {

    @GetMapping("/external/{userId}")
    User findUserFromMicroserviceUser(@PathVariable Long userId);

    @PostMapping("/external/")
    void saveUserAccountFromMicroserviceUser(@RequestParam Long accountId, @RequestParam Long userId);


    @DeleteMapping("/external/")
    void deleteAccountUserFromMicroserviceUser(@RequestParam Long userId , @RequestParam Long accountId);


}
