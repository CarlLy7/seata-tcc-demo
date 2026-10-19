package com.carl.service;

import org.apache.seata.rm.tcc.api.BusinessActionContext;
import org.apache.seata.rm.tcc.api.BusinessActionContextParameter;
import org.apache.seata.rm.tcc.api.LocalTCC;
import org.apache.seata.rm.tcc.api.TwoPhaseBusinessAction;

import java.math.BigDecimal;

/**
 * @description:
 * @author: carl
 * @date: 2026.10.16
 * @Since: 1.0
 */
@LocalTCC //如果 TCC 参与者是本地 bean（非远程RPC服务），本地 TCC bean 还需要在接口定义中添加 @LocalTCC 注解
public interface AccountTccService {

    /**
     * 从 Seata 1.5.0 开始，官方对此进行了优化：
     * TCC 模式一阶段方法进行了优化，不再需要在一阶段的接口入参定义 BusinessActionContext，
     * 若一阶段需要使用到 BusinessActionContext，可以通过 BusinessActionContextUtil.getContext() 取得。
     * @param userId
     * @param balance
     * @return
     */
    @TwoPhaseBusinessAction(name = "updateAccountBalance",commitMethod = "confirm",rollbackMethod = "cancel",useTCCFence = true)
    public boolean updateAccountBalance(
                    @BusinessActionContextParameter(value = "userId") Integer userId,
                    @BusinessActionContextParameter(value = "balance")BigDecimal balance);

    public boolean confirm(BusinessActionContext actionContext);

    public boolean cancel(BusinessActionContext actionContext);
}
