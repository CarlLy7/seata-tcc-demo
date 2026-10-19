package com.carl.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.carl.domain.BusinessOrder;
import com.carl.domain.Order;
import com.carl.service.BusinessOrderService;
import com.carl.service.CreateOrderTccService;
import jakarta.annotation.Resource;
import org.apache.seata.rm.tcc.api.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * @description:
 * @author: carl
 * @date: 2026.10.19
 * @Since: 1.0
 */
@Service
@LocalTCC
public class CreateOrderTccServiceImpl implements CreateOrderTccService {

    @Resource
    private BusinessOrderService businessOrderService;

    @Override
    @TwoPhaseBusinessAction(name = "prepareCreateOrder", commitMethod = "confirm", rollbackMethod = "cancel", useTCCFence = true)
    public boolean prepareCreateOrder(@BusinessActionContextParameter(value = "order") Order order) {
        BusinessActionContext context = BusinessActionContextUtil.getContext();
        String xid = context.getXid();
        BusinessOrder businessOrder = BeanUtil.copyProperties(order, BusinessOrder.class);
        businessOrder.setXid(xid);
        businessOrder.setCreateTime(LocalDateTime.now());
        businessOrder.setStatus("CREATED");
        return false;
    }

    @Override
    public boolean confirm(BusinessActionContext context) {
        Order order = context.getActionContext("order", Order.class);
        Integer userId = order.getUserId();
        Integer productId = order.getProductId();
        String xid = context.getXid();
        return businessOrderService.update(Wrappers.lambdaUpdate(BusinessOrder.class)
                .eq(BusinessOrder::getXid, xid)
                .eq(BusinessOrder::getUserId, userId)
                .eq(BusinessOrder::getProductId, productId)
                .set(BusinessOrder::getStatus, "CONFIRM"));
    }

    @Override
    public boolean cancel(BusinessActionContext context) {
        String xid = context.getXid();
        BusinessOrder businessOrder = businessOrderService.getOne(Wrappers.lambdaQuery(BusinessOrder.class)
                .eq(BusinessOrder::getXid, xid));
        businessOrder.setStatus("CANCEL");
        return businessOrderService.updateById(businessOrder);
    }
}
