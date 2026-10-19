package com.carl.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.carl.domain.UserAccount;
import com.carl.service.UserAccountService;
import com.carl.mapper.UserAccountMapper;
import org.springframework.stereotype.Service;

/**
* @author carl
* @description 针对表【user_account】的数据库操作Service实现
* @createDate 2026-10-16 15:19:47
*/
@Service
public class UserAccountServiceImpl extends ServiceImpl<UserAccountMapper, UserAccount>
    implements UserAccountService{

}




