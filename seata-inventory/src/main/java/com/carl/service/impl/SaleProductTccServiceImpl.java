package com.carl.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.carl.mapper.ProductInventoryMapper;
import com.carl.service.SaleProductTccService;
import lombok.extern.slf4j.Slf4j;
import org.apache.seata.rm.tcc.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @description:
 * @author: carl
 * @date: 2026.10.16
 * @Since: 1.0
 */
@Service
@Slf4j
@LocalTCC //社区建议将注解从接口层下沉到实现层
public class SaleProductTccServiceImpl implements SaleProductTccService {
    @Autowired
    private ProductInventoryMapper productInventoryMapper;


    /**
     * try阶段
     * 增加锁定数量
     *
     * @param productId
     * @param saleQty
     * @return
     */
    @Override
    @TwoPhaseBusinessAction(name = "saleProduct", commitMethod = "confirm", rollbackMethod = "cancel", useTCCFence = true)
    public boolean saleProduct(@BusinessActionContextParameter(value = "productId") Integer productId,
                               @BusinessActionContextParameter(value = "saleQty") Integer saleQty) {

        /**
         * 业务悬挂问题
         * 注意：我们开启了useTCCFence，框架会自动帮我们处理业务悬挂问题
         */
//        ProductLocked productLocked = productLockedService.getById(xid);
//        //如果存在锁定信息
//        if (productLocked != null) {
//            log.error("空回滚了，不允许try");
//            return false;
//        }

        //锁定库存数量，锁定数量增加
        int updateRows = productInventoryMapper.lockedQty(productId, saleQty);
        if (updateRows == 0) {
            System.out.println("锁定库存数量失败");
            return false;
        }
        return true;


    }

    /**
     * confirm阶段
     * 锁定库存数量扣减、可用库存数量扣减
     * @param actionContext
     * @return
     */
    @Override
    public boolean confirm(BusinessActionContext actionContext) {

        Integer productId = actionContext.getActionContext("productId", Integer.class);
        Integer saleQty = actionContext.getActionContext("saleQty", Integer.class);
        int rows = productInventoryMapper.decreaseProductInventory(productId, saleQty);
        if(rows==0){
            System.out.println("扣减库存失败");
            return false;
        }
        return true;
    }

    /**
     * cancel阶段
     * 扣减锁定数量
     * @param actionContext
     * @return
     */
    @Override
    public boolean cancel(BusinessActionContext actionContext) {

        //空回滚，useTCCFence开启后框架会帮我们自动实现
//        if (productLocked == null) {
//            Integer saleQty = actionContext.getActionContext("saleQty", Integer.class);
//            //创建商品锁定数据
//            ProductLocked locked = new ProductLocked();
//            locked.setXid(xid);
//            Integer productId = actionContext.getActionContext("productId", Integer.class);
//            locked.setProductId(productId);
//            locked.setLockedQty(saleQty);
//            locked.setLockedStatus(2);
//            return productLockedService.save(locked);
//        }

        Integer productId = actionContext.getActionContext("productId", Integer.class);
        Integer saleQty = actionContext.getActionContext("saleQty", Integer.class);
        int updateRows = productInventoryMapper.decreaseLockedQty(productId, saleQty);
        if (updateRows==0){
            System.out.println("释放锁定数量失败");
            return false;
        }
        return true;
    }
}
