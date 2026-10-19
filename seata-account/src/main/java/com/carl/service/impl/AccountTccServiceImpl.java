package com.carl.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.carl.domain.AccountFreeze;
import com.carl.mapper.UserAccountMapper;
import com.carl.service.AccountFreezeService;
import com.carl.service.AccountTccService;
import lombok.extern.slf4j.Slf4j;
import org.apache.seata.rm.tcc.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Autowired
    private AccountFreezeService accountFreezeService;

    /**
     * try阶段
     * useTCCFence=true,框架会自动帮我们处理空回滚、业务悬挂问题。但是需要在数据库中创建tcc_fence_log表
     *
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

        //创建账户锁定数据
        AccountFreeze accountFreeze = new AccountFreeze();
        accountFreeze.setXid(xid);
        accountFreeze.setUserId(userId);
        accountFreeze.setFreezeMoney(balance);
        accountFreeze.setFreezeStatus(0);
        return accountFreezeService.save(accountFreeze);
    }

    /**
     * confirm阶段
     *
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean confirm(BusinessActionContext actionContext) {
        String xid = actionContext.getXid();
        //先清除锁定记录
        accountFreezeService.removeById(xid);

        Integer userId = actionContext.getActionContext("userId", Integer.class);
        BigDecimal balance = actionContext.getActionContext("balance", BigDecimal.class);
        int updateRows = userAccountMapper.deduceBalance(userId, balance);
        if (updateRows == 0) {
            throw new RuntimeException("更新账户余额失败");
        }
        return true;
    }

    /**
     * cancel阶段
     *
     * @param actionContext
     * @return
     */
    @Override
    public boolean cancel(BusinessActionContext actionContext) {
        /**
         * 空回滚,useTCCFence=true的话框架自动帮我们实现
         */
        String xid = actionContext.getXid();
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

        return accountFreezeService.update(Wrappers.lambdaUpdate(AccountFreeze.class)
                .eq(AccountFreeze::getXid, xid)
                .set(AccountFreeze::getFreezeStatus, 2));
    }
}
