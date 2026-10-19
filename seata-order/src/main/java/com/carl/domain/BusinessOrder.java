package com.carl.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName business_order
 */
@TableName(value ="business_order")
@Data
public class BusinessOrder {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 
     */
    private Integer userId;

    /**
     * 
     */
    private Integer productId;

    /**
     * 
     */
    private Integer saleQty;

    private BigDecimal price;

    /**
     * 事务id
     */
    private String xid;

    /**
     * 
     */
    private LocalDateTime createTime;

    private String status;
}