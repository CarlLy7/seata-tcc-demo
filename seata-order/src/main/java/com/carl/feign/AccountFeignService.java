package com.carl.feign;

import com.carl.domain.FeignDeduceAccountParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @description:
 * @author: carl
 * @date: 2026.10.16
 * @Since: 1.0
 */
@FeignClient(url = "http://localhost:8083/account", contextId = "accountFeignService",name = "account")
public interface AccountFeignService {
    @PostMapping("/deduce")
    public boolean deduceAccount(@RequestBody FeignDeduceAccountParam param);
}
