package com.carl.mapper;

import com.carl.domain.ProductLocked;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author carl
* @description 针对表【product_locked】的数据库操作Mapper
* @createDate 2026-10-16 16:00:58
* @Entity com.carl.domain.ProductLocked
*/
@Mapper
public interface ProductLockedMapper extends BaseMapper<ProductLocked> {

}




