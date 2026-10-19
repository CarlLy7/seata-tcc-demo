package com.carl.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;
import lombok.Data;

/**
 * 
 * @TableName account_freeze
 */
@TableName(value ="account_freeze")
@Data
public class AccountFreeze {
    /**
     * 
     */
    @TableId(type = IdType.INPUT)
    private String xid;

    /**
     * 
     */
    private Integer userId;

    /**
     * 
     */
    private BigDecimal freezeMoney;

    /**
     * 0:try,1:commit,2:cancel
     */
    private Integer freezeStatus;
}