package com.carl.mapper;

import com.carl.domain.UserAccount;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;

/**
* @author carl
* @description 针对表【user_account】的数据库操作Mapper
* @createDate 2026-10-16 15:19:47
* @Entity com.carl.domain.UserAccount
*/
@Mapper
public interface UserAccountMapper extends BaseMapper<UserAccount> {

    int deduceBalance(@Param("userId") Integer userId, @Param("balance") BigDecimal balance);

    int freezeBalance(@Param("userId") Integer userId, @Param("freezeBalance") BigDecimal balance);

    int increaseBalance(@Param("userId") Integer userId, @Param("balance") BigDecimal balance);
}




