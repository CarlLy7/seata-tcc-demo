package com.carl;


import com.carl.domain.Order;
import com.carl.service.OrderService;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

@SpringBootTest
public class SeataOrderApplicationTest {

    @Resource
    private OrderService orderService;

    @Test
    public void testCreateOrder() {
        Order order = new Order();
        order.setUserId(1);
        order.setProductId(1);
        order.setPrice(BigDecimal.valueOf(100));
        order.setSaleQty(10);

        orderService.createOrder(order);
    }
}
