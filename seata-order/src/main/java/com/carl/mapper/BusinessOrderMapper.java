package com.carl.mapper;

import com.carl.domain.BusinessOrder;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author carl
* @description 针对表【business_order】的数据库操作Mapper
* @createDate 2026-10-16 16:40:03
* @Entity com.carl.domain.BusinessOrder
*/
@Mapper
public interface BusinessOrderMapper extends BaseMapper<BusinessOrder> {

}




