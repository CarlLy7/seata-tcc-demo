package com.carl.controller;

import cn.hutool.core.exceptions.ExceptionUtil;
import com.carl.domain.DeduceAccountParam;
import com.carl.service.AccountTccService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description:
 * @author: carl
 * @date: 2026.10.16
 * @Since: 1.0
 */
@RestController
@RequestMapping("/account")
public class AccountController {

    @Resource
    private AccountTccService accountTccService;

    @PostMapping("/deduce")
    public boolean deduceAccount(@RequestBody DeduceAccountParam param){
        try{
            boolean updateAccountBalance = accountTccService.updateAccountBalance(param.getUserId(), param.getBalance());
            System.out.println("deduceAccount res="+updateAccountBalance);
            return updateAccountBalance;
        }catch (Exception e){
            System.out.println("deduceAccount error msg:"+ExceptionUtil.getMessage(e));
            return false;
        }
    }
}
