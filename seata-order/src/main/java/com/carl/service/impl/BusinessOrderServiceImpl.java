package com.carl.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.carl.domain.BusinessOrder;
import com.carl.service.BusinessOrderService;
import com.carl.mapper.BusinessOrderMapper;
import org.springframework.stereotype.Service;

/**
* @author carl
* @description 针对表【business_order】的数据库操作Service实现
* @createDate 2026-10-16 16:40:03
*/
@Service
public class BusinessOrderServiceImpl extends ServiceImpl<BusinessOrderMapper, BusinessOrder>
    implements BusinessOrderService{

}




