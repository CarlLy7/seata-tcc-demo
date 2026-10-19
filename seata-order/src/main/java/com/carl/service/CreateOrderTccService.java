package com.carl.service;

import com.carl.domain.Order;
import org.apache.seata.rm.tcc.api.BusinessActionContext;
import org.apache.seata.rm.tcc.api.BusinessActionContextParameter;
import org.apache.seata.rm.tcc.api.TwoPhaseBusinessAction;

/**
 * @description:
 * @author: carl
 * @date: 2026.10.19
 * @Since: 1.0
 */
public interface CreateOrderTccService {

    @TwoPhaseBusinessAction(name = "prepareCreateOrder",commitMethod = "confirm",rollbackMethod = "cancel",useTCCFence = true)
    public boolean prepareCreateOrder(@BusinessActionContextParameter(value = "order")Order order);

    public boolean confirm(BusinessActionContext context);

    public boolean cancel(BusinessActionContext context);
}
