package com.carl.service.impl;

import com.carl.mapper.UserAccountMapper;
import com.carl.service.AccountTccService;
import lombok.extern.slf4j.Slf4j;
import org.apache.seata.rm.tcc.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * @description:
 * @author: carl
 * @date: 2026.10.16
 * @Since: 1.0
 */
@Service
@Slf4j
@LocalTCC
public class AccountTccServiceImpl implements AccountTccService {

    @Autowired
    private UserAccountMapper userAccountMapper;

    /**
     * try阶段
     * useTCCFence=true,框架会自动帮我们处理空回滚、业务悬挂问题。但是需要在数据库中创建tcc_fence_log表
     * TCC的一个注意点：confirm阶段执行失败会不断重试，不会执行cancel。
     * Confirm 的设计契约是：“Try 成功，则 Confirm 必须成功”。Confirm 阶段不应再出现业务逻辑上的失败。
     * @param userId
     * @param balance
     * @return
     */
    @Override
    //@TwoPhaseBusinessAction会把当前这个事务作为一个事务分支注册到TM中
    @TwoPhaseBusinessAction(name = "updateAccountBalance", commitMethod = "confirm", rollbackMethod = "cancel", useTCCFence = true)
    public boolean updateAccountBalance(@BusinessActionContextParameter(value = "userId") Integer userId,
                                        @BusinessActionContextParameter(value = "balance") BigDecimal balance) {

        BusinessActionContext context = BusinessActionContextUtil.getContext();
        String xid = context.getXid();

        /**
         * 业务悬挂问题,useTCCFence=true的话，框架自动帮我们实现
         */
//        AccountFreeze freeze = accountFreezeService.getById(xid);
        //存在当前用户的冻结数据
//        if (freeze != null) {
//            log.error("空回滚了，不允许try");
//            return false;
//        }

        //冻结账户余额,冻结余额增加
        int updateRows=userAccountMapper.freezeBalance(userId,balance);
        if (updateRows==0){
            log.error("冻结余额失败");
            return false;
        }
        return true;
    }

    /**
     * confirm阶段
     * 余额扣减、冻结金额扣减
     * @return
     */
    @Override
    public boolean confirm(BusinessActionContext actionContext) {
        Integer userId = actionContext.getActionContext("userId", Integer.class);
        BigDecimal balance = actionContext.getActionContext("balance", BigDecimal.class);
        int updateRows = userAccountMapper.deduceBalance(userId, balance);
        if (updateRows == 0) {
            throw new RuntimeException("更新账户余额失败");
        }
        return true;
    }

    /**
     * cancel阶段，try阶段失败后调用
     *
     * @param actionContext
     * @return
     */
    @Override
    public boolean cancel(BusinessActionContext actionContext) {
        /**
         * 空回滚,useTCCFence=true的话框架自动帮我们实现
         */
        Integer userId = actionContext.getActionContext("userId", Integer.class);
        BigDecimal balance = actionContext.getActionContext("balance", BigDecimal.class);
//        AccountFreeze accountFreeze = accountFreezeService.getById(xid);
//        if (accountFreeze == null) {
//            创建账户锁定数据
//            BigDecimal balance = actionContext.getActionContext("balance", BigDecimal.class);
//            AccountFreeze saveAccountFreeze = new AccountFreeze();
//            saveAccountFreeze.setXid(xid);
//            Integer userId = actionContext.getActionContext("userId", Integer.class);
//            saveAccountFreeze.setUserId(userId);
//            saveAccountFreeze.setFreezeMoney(balance);
//            saveAccountFreeze.setFreezeStatus(2);
//            return accountFreezeService.save(saveAccountFreeze);
//        }

        //冻结金额扣减
         int updateRows=userAccountMapper.increaseBalance(userId,balance);
         if (updateRows == 0) {
             return false;
         }
         return true;
    }
}
