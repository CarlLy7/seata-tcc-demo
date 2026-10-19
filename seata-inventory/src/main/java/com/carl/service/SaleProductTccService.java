package com.carl.service;

import org.apache.seata.rm.tcc.api.BusinessActionContext;
import org.apache.seata.rm.tcc.api.BusinessActionContextParameter;
import org.apache.seata.rm.tcc.api.LocalTCC;
import org.apache.seata.rm.tcc.api.TwoPhaseBusinessAction;

/**
 * @description:
 * @author: carl
 * @date: 2026.10.16
 * @Since: 1.0
 */
@LocalTCC
public interface SaleProductTccService {

    @TwoPhaseBusinessAction(name = "saleProduct", commitMethod = "confirm", rollbackMethod = "cancel",useTCCFence=true)
    public boolean saleProduct(@BusinessActionContextParameter(value = "productId") Integer productId,
                               @BusinessActionContextParameter(value = "saleQty") Integer saleQty);

    public boolean confirm(BusinessActionContext actionContext);

    public boolean cancel(BusinessActionContext actionContext);
}
