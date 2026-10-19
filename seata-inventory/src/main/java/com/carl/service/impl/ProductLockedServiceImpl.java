package com.carl.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.carl.domain.ProductLocked;
import com.carl.service.ProductLockedService;
import com.carl.mapper.ProductLockedMapper;
import org.springframework.stereotype.Service;

/**
* @author carl
* @description 针对表【product_locked】的数据库操作Service实现
* @createDate 2026-10-16 16:00:58
*/
@Service
public class ProductLockedServiceImpl extends ServiceImpl<ProductLockedMapper, ProductLocked>
    implements ProductLockedService{

}




