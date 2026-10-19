package com.carl.service.impl;

import com.carl.domain.Order;
import com.carl.service.CreateOrderTccService;
import com.carl.service.OrderService;
import jakarta.annotation.Resource;
import org.apache.seata.spring.annotation.GlobalTransactional;
import org.springframework.stereotype.Service;

/**
 * @description:
 * @author: carl
 * @date: 2026.10.16
 * @Since: 1.0
 */
@Service
public class OrderServiceImpl implements OrderService {

    @Resource
    private CreateOrderTccService createOrderTccService;

    @Override
    public void createOrder(Order order) {
    }
}
