package com.carl.controller;

import cn.hutool.core.exceptions.ExceptionUtil;
import com.carl.domain.SaleProductParam;
import com.carl.service.SaleProductTccService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description:
 * @author: carl
 * @date: 2026.10.16
 * @Since: 1.0
 */
@RestController
@RequestMapping("/inventory")
public class InventoryController {

    @Resource
    private SaleProductTccService saleProductTccService;

    @PostMapping("/saleProduct")
    public boolean saleProduct(@RequestBody SaleProductParam param) {
        try {
            boolean saleProductRes = saleProductTccService.saleProduct( param.getProductId(), param.getSaleQty());
            System.out.println("saleProduct res=" + saleProductRes);
            return saleProductRes;
        } catch (Exception e) {
            System.out.println("saleProduct error msg:" + ExceptionUtil.getMessage(e));
            return false;
        }
    }
}
