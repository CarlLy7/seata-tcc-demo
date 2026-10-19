package com.carl.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.carl.domain.ProductLocked;
import com.carl.mapper.ProductInventoryMapper;
import com.carl.service.ProductLockedService;
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


    @Autowired
    private ProductLockedService productLockedService;

    /**
     * try阶段
     *
     * @param productId
     * @param saleQty
     * @return
     */
    @Override
    @TwoPhaseBusinessAction(name = "saleProduct", commitMethod = "confirm", rollbackMethod = "cancel", useTCCFence = true)
    public boolean saleProduct(@BusinessActionContextParameter(value = "productId") Integer productId,
                               @BusinessActionContextParameter(value = "saleQty") Integer saleQty) {

        BusinessActionContext context = BusinessActionContextUtil.getContext();
        String xid = context.getXid();
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

        //创建锁定库存数据

        ProductLocked locked = new ProductLocked();
        locked.setXid(xid);
        locked.setProductId(productId);
        locked.setLockedQty(saleQty);
        locked.setLockedStatus(0);
        return productLockedService.save(locked);


    }

    /**
     * confirm阶段
     *
     * @param actionContext
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean confirm(BusinessActionContext actionContext) {
        String xid = actionContext.getXid();
        //删除锁定记录
        productLockedService.removeById(xid);

        Integer productId = actionContext.getActionContext("productId", Integer.class);
        Integer saleQty = actionContext.getActionContext("saleQty", Integer.class);
        int rows = productInventoryMapper.decreaseProductInventory(productId, saleQty);
        return rows != 0;
    }

    /**
     * cancel阶段
     *
     * @param actionContext
     * @return
     */
    @Override
    public boolean cancel(BusinessActionContext actionContext) {

        String xid = actionContext.getXid();
        ProductLocked productLocked = productLockedService.getById(xid);
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

        return productLockedService.update(Wrappers.lambdaUpdate(ProductLocked.class)
                .eq(ProductLocked::getXid, xid)
                .set(ProductLocked::getLockedStatus, 2));
    }
}
