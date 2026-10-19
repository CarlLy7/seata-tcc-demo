package com.carl.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 
 * @TableName product_inventory
 */
@TableName(value ="product_inventory")
@Data
public class ProductInventory {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 
     */
    private Integer productId;

    /**
     * 可用库存
     */
    private Integer qty;

    /**
     * 锁定库存
     */
    private Integer lockedQty;
}