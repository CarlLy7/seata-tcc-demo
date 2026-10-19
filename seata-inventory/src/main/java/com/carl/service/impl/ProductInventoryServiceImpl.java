package com.carl.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.carl.domain.ProductInventory;
import com.carl.service.ProductInventoryService;
import com.carl.mapper.ProductInventoryMapper;
import org.springframework.stereotype.Service;

/**
* @author carl
* @description 针对表【product_inventory】的数据库操作Service实现
* @createDate 2026-10-16 16:00:58
*/
@Service
public class ProductInventoryServiceImpl extends ServiceImpl<ProductInventoryMapper, ProductInventory>
    implements ProductInventoryService{

}




