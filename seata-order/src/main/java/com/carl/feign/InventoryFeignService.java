package com.carl.feign;

import com.carl.domain.FeignSaleProductParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @description:
 * @author: carl
 * @date: 2026.10.16
 * @Since: 1.0
 */
@FeignClient(url = "http://localhost:8085/inventory", contextId = "inventoryFeignService",name = "inventory")
public interface InventoryFeignService {
    @PostMapping("/saleProduct")
    public boolean saleProduct(@RequestBody FeignSaleProductParam param);
}
