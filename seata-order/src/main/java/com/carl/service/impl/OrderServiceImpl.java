package com.carl.service.impl;

import com.carl.domain.BusinessOrder;
import com.carl.domain.FeignDeduceAccountParam;
import com.carl.domain.FeignSaleProductParam;
import com.carl.domain.Order;
import com.carl.feign.AccountFeignService;
import com.carl.feign.InventoryFeignService;
import com.carl.service.BusinessOrderService;
import com.carl.service.OrderService;
import jakarta.annotation.Resource;
import org.apache.seata.spring.annotation.GlobalTransactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * @description: 下单入口，作为全局事务
 * @author: carl
 * @date: 2026.10.16
 * @Since: 1.0
 */
@Service
public class OrderServiceImpl implements OrderService {

    @Resource
    private AccountFeignService accountFeignService;

    @Resource
    private InventoryFeignService inventoryFeignService;

    @Resource
    private BusinessOrderService businessOrderService;

    @Override
    @GlobalTransactional(name = "createOrder",rollbackFor = Exception.class)
    public void createOrder(Order order) {
        FeignDeduceAccountParam feignDeduceAccountParam = new FeignDeduceAccountParam();
        feignDeduceAccountParam.setUserId(order.getUserId());
        feignDeduceAccountParam.setBalance(order.getPrice());
        boolean deduceAccountRes = accountFeignService.deduceAccount(feignDeduceAccountParam);
        if (!deduceAccountRes) {
            System.out.println("deduceAccount failed");
            return;
        }

        FeignSaleProductParam feignSaleProductParam = new FeignSaleProductParam();
        feignSaleProductParam.setProductId(order.getProductId());
        feignSaleProductParam.setSaleQty(order.getSaleQty());
        boolean saleProductRes = inventoryFeignService.saleProduct(feignSaleProductParam);
        if (!saleProductRes) {
            System.out.println("saleProduct failed");
            return;
        }

        BusinessOrder businessOrder = new BusinessOrder();
        businessOrder.setUserId(order.getUserId());
        businessOrder.setProductId(order.getProductId());
        businessOrder.setSaleQty(order.getSaleQty());
        businessOrder.setPrice(order.getPrice());
        businessOrder.setCreateTime(LocalDateTime.now());
        businessOrder.setStatus("CREATED");
        businessOrderService.save(businessOrder);
    }
}
