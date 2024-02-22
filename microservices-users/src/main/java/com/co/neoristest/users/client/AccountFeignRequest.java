package com.co.neoristest.users.client;


import com.co.neoristest.users.domain.AccountDetail;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "microservices-account", url = "${microservices.account.url}/cuentas")
public interface AccountFeignRequest {

    @GetMapping("/external/")
    List<AccountDetail> getAllAccoutDetail(@RequestParam Iterable<Long> accountsIds);

}
