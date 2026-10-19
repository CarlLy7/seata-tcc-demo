package com.carl.mapper;

import com.carl.domain.ProductInventory;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
* @author carl
* @description 针对表【product_inventory】的数据库操作Mapper
* @createDate 2026-10-16 16:00:58
* @Entity com.carl.domain.ProductInventory
*/
@Mapper
public interface ProductInventoryMapper extends BaseMapper<ProductInventory> {

    int decreaseProductInventory(@Param("productId") Integer productId, @Param("saleQty") Integer saleQty);

    int lockedQty(@Param("productId") Integer productId, @Param("lockedQty") Integer saleQty);

    int decreaseLockedQty(@Param("productId") Integer productId, @Param("saleQty") Integer saleQty);
}




