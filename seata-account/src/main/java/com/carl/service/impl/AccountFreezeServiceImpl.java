package com.carl.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.carl.domain.AccountFreeze;
import com.carl.service.AccountFreezeService;
import com.carl.mapper.AccountFreezeMapper;
import org.springframework.stereotype.Service;

/**
* @author carl
* @description 针对表【account_freeze】的数据库操作Service实现
* @createDate 2026-10-16 15:33:45
*/
@Service
public class AccountFreezeServiceImpl extends ServiceImpl<AccountFreezeMapper, AccountFreeze>
    implements AccountFreezeService{

}




