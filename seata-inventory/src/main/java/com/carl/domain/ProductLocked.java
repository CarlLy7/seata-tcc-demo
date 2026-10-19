package com.carl.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 
 * @TableName product_locked
 */
@TableName(value ="product_locked")
@Data
public class ProductLocked {
    /**
     * 
     */
    @TableId(type = IdType.INPUT)
    private String xid;

    /**
     * 
     */
    private Integer productId;

    /**
     * 
     */
    private Integer lockedQty;

    /**
     * 
     */
    private Integer lockedStatus;
}